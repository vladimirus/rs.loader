curl -XDELETE "http://localhost:9200/rs/"

curl -XPUT "http://localhost:9200/rs/"

curl -XPUT "http://localhost:9200/rs/_mapping/topic" -d'
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

curl -XPUT "http://localhost:9200/rs/_mapping/link" -d'
{
  "link": {
    "_timestamp": {
      "enabled": true,
      "store": true
    },
    "properties": {
      "title": {
        "type": "string",
        "analyzer": "english"
      },
      "created": {
        "type": "date"
      }
    }
  }
}'

curl -XPUT "http://localhost:9200/rs/_mapping/suggestion" -d '
{
  "suggestion" : {
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

