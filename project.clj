(defproject skeleton "1.0.0-SNAPSHOT"
  :description "Skeleton project for full stack clojure application."
  :jvm-opts ["-Xmx1g" "-server"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.datomic/datomic-free "0.9.5153" :exclusions [joda-time]]
                 [compojure "1.3.2"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-defaults "0.1.4"]
                 [org.clojure/clojurescript "0.0-3178"]
                 [org.omcljs/om "0.8.8"]
                 [sablono "0.3.4"]
                 [figwheel "0.3.3"]]

  :plugins [[lein-ring "0.8.13"]
            [lein-cljsbuild "1.0.5"]
            [lein-figwheel "0.3.3"]]

  :ring {:handler app.core/app
         :port 3000 }

  :source-paths ["src/clj"]

  :clean-targets []

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/out/app.js"
                                   :output-dir "resources/public/out"
                                   :optimizations :none
                                   :cache-analysis true
                                   :source-map true}}]})

