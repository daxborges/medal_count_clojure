(defproject medal_count_clj "0.1.0-SNAPSHOT"

  :description "Medal Counts of the world olympics"

  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure        "1.8.0"]
                 [org.clojure/clojurescript  "1.9.89"]
                 [bidi "2.1.2"]
                 [cljs-ajax "0.7.0"]
                 [reagent "0.6.0-rc"]
                 [re-frame "0.9.4"]
                 [day8.re-frame/http-fx "0.1.4"]
                 [binaryage/devtools "0.8.1"]]

  :plugins [[lein-doo "0.1.7"]
            [lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.6"]]

  :hooks [leiningen.cljsbuild]

  :profiles {:dev  {:cljsbuild
                    {:builds {:client {:compiler {:asset-path           "js"
                                                  :optimizations        :none
                                                  :source-map           true
                                                  :source-map-timestamp true
                                                  :main                 "medal-count-clj.core"}
                                       :figwheel {:on-jsload "medal-count-clj.core/main"}}}}}

             :test {:cljsbuild
                    {:builds {:test {:compiler {:optimizations :none
                                                  :pretty-print  false
                                                  :main          "medal-count-clj.browser"}}}}}

             :prod {:cljsbuild
                    {:builds {:client {:compiler {:optimizations :advanced
                                                  :elide-asserts true
                                                  :pretty-print  false}}}}}}

  :figwheel {:server-port 3450
             :repl        true
             :css-dirs    ["resources/public/css"]}


  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  :cljsbuild {:builds {:client {:source-paths ["src"]
                                :compiler     {:output-dir "resources/public/js"
                                               :output-to  "resources/public/js/client.js"}}
                       :test {:source-paths ["src" "test"]
                              :compiler {:output-dir "resources/public/js/test"
                                         :output-to  "resources/public/js/client-test.js"
                                         ;:main 'lab-notebook.browser
                                         ;:optimizations :none
                                         }}}})
