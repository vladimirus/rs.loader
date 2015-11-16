package rs.loader.job;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.model.Link;
import rs.loader.service.LinkManager;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class MissingDomainsLoaderJob {
    private Logger log = Logger.getLogger(MissingDomainsLoaderJob.class);
    @Autowired
    private LinkManager linkManager;
    @Value("${rs.loader.link.domain.missing.enabled:true}")
    boolean enabled;

    @Scheduled(initialDelay = 100000, fixedRate = 10000)
    public void load() {
        if (enabled) {
            log.debug("Staring CommentMissingLoaderJob");
            linkManager.save(linkManager.getMissingDomains(0, 1000).stream()
                    .map(link -> populateDomain(link, validateImgurDomain(getDomain(link.getUrl()))))
                    .filter(link -> link.getDomain() != null)
                    .peek(link -> log.debug(format("domain missing for %s fixing...", link.getId())))
                    .collect(toList()));
        }
    }

    private Link populateDomain(Link link, String domain) {
        link.setDomain(domain);
        return link;
    }

    String validateImgurDomain(String url) {
        return ofNullable(url)
                .filter(u -> u.contains("imgur.com"))
                .map(u -> "i.imgur.com")
                .orElse(url);
    }

    String getDomain(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
