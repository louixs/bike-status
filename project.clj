(defproject bike-status "0.1.0-SNAPSHOT"
  :description "City bike status availability - London"
  :url "http://example.com/FIXME"
  :license {:name "Apache License 2.0"
            :url "http://choosealicense.com/licenses/apache-2.0/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.11.0"]
                 [com.draines/postal "1.11.3"]
                 [org.immutant/immutant "2.1.4"]
                 [com.github.kyleburton/clj-xpath "1.4.3"]
                 ]
  :main ^:skip-aot bikes.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :immutant {:nrepl-port 24005})
