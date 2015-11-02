## Rds to Gorkana transfer service

### Project setup:
* Install Scala 2.11
* Checkout project:
* Import to Intellij as SBT project
* Run ```ApplicationMain```

* Run monitoring Docker image:
Kamon.io + Statsd + Graphite: docker run -d -p 80:80 -p 8125:8125/udp -p 8126:8126 --name kamon-grafana-dashboard kamon/grafana_graphite

