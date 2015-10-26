#!/usr/bin/env bash

curl -XDELETE "http://localhost:9200/rs3"

curl -XPUT "http://localhost:9200/rs3" -d'
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 1,
    "analysis": {
      "filter": {
        "ngram_filter": {
          "type": "edge_ngram",
          "min_gram": 2,
          "max_gram": 20
        }
      },
      "analyzer": {
        "ngram_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "ngram_filter"
          ]
        }
      }
    }
  }
}'

curl -XPUT "http://localhost:9200/rs3/_mapping/topic" -d'
{
  "topic": {
    "_timestamp": {
      "enabled": true,
      "store": true
    },
    "properties": {
      "created": {
        "type": "date"
      },
      "updated": {
        "type": "date"
      }
    }
  }
}'

curl -XPUT "http://localhost:9200/rs3/_mapping/link" -d'
{
  "link": {
    "_timestamp": {
      "enabled": true,
      "store": true
    },
    "properties": {
      "id": {
        "type": "string",
        "index": "not_analyzed"
      },
      "title": {
        "type": "string",
        "index_analyzer": "ngram_analyzer",
        "search_analyzer": "standard"
      },
      "url": {
        "type": "string",
        "index": "not_analyzed"
      },
      "commentsUrl": {
        "type": "string",
        "index": "not_analyzed"
      },
      "author": {
        "type": "string",
        "index": "not_analyzed"
      },
      "topic": {
        "type": "string",
        "index": "not_analyzed"
      },
      "topicId": {
        "type": "string",
        "index": "not_analyzed"
      },
      "commentCount": {
        "type": "integer",
        "index": "not_analyzed"
      },
      "score": {
        "type": "integer",
        "index": "not_analyzed"
      },
      "created": {
        "type": "date"
      },
      "self": {
        "type": "boolean",
        "index": "not_analyzed"
      },
      "nsfw": {
        "type": "boolean",
        "index": "not_analyzed"
      },
      "hidden": {
        "type": "boolean",
        "index": "not_analyzed"
      },
      "thumbnail": {
        "type": "string",
        "index": "not_analyzed"
      },
      "selfText": {
        "type": "string",
        "index": "not_analyzed"
      },
      "selfTextHtml": {
        "type": "string",
        "index": "not_analyzed"
      },
      "domain": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.id": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.author": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.parentId": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.topic": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.topicId": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.linkId": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.bodyHTML": {
        "type": "string",
        "index": "not_analyzed"
      },
      "comments.body": {
        "type": "string",
        "index_analyzer": "ngram_analyzer",
        "search_analyzer": "standard"
      },
      "comments.created": {
        "type": "date"
      },
      "comments.score": {
        "type": "integer",
        "index": "not_analyzed"
      }
    }
  }
}'

curl -XPUT "http://localhost:9200/rs3/_mapping/comment" -d'
{
  "comment": {
    "_timestamp": {
      "enabled": true,
      "store": true
    },
    "properties": {
      "created": {
        "type": "date"
      }
    }
  }
}'

curl -XDELETE "http://localhost:9200/rs2/_mapping/suggestion"
curl -XPUT "http://localhost:9200/rs2/_mapping/suggestion" -d '
{
  "suggestion" : {
    "_ttl" : { "enabled" : true, "default" : "1d" },
    "properties" : {
      "original" : { "type" : "string" },
      "suggest" : {
        "type" :            "completion",
        "index_analyzer" :  "english",
        "search_analyzer" : "standard",
        "preserve_position_increments": false,
        "preserve_separators": false
      }
    }
  }
}'
