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