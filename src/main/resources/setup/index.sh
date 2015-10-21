#!/usr/bin/env bash

curl -XDELETE "http://localhost:9200/rs2"

curl -XPUT "http://localhost:9200/rs2" -d'
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

curl -XPUT "http://localhost:9200/rs2/_mapping/topic" -d'
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

curl -XPUT "http://localhost:9200/rs2/_mapping/link" -d'
{
  "link": {
    "_timestamp": {
      "enabled": true,
      "store": true
    },
    "properties": {
      "title": {
        "type": "string",
        "index_analyzer": "ngram_analyzer",
        "search_analyzer": "standard"
      },
      "created": {
        "type": "date"
      }
    }
  }
}'

curl -XPUT "http://localhost:9200/rs2/_mapping/comment" -d'
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

# curl -XDELETE "http://localhost:9200/rs2/_mapping/suggestion"
curl -XPUT "http://localhost:9200/rs2/_mapping/suggestion" -d '
{
  "suggestion" : {
    "_ttl" : { "enabled" : true, "default" : "6w" },
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
