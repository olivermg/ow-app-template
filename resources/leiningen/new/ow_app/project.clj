(defproject {{project-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.6.1"

  :dependencies [;;; core:
                 [org.clojure/clojure       "1.8.0"]    ;; the language (jvm)
                 [org.clojure/clojurescript "1.9.562"]  ;; the language (js)
                 [org.clojure/core.async    "0.2.395"]  ;; async communication channels
                 [environ                   "1.0.3"]    ;; handling environment vars

                 ;;; program flow / lifecycle:
                 [integrant    "0.4.1"]           ;; component lifecycle management
                 [clj-starbuck "0.2.0-SNAPSHOT"]  ;; message based component communications
                 [re-frame     "0.9.1"]           ;; browser program flow

                 ;;; http:
                 [ring-core              "1.4.0"]  ;; basic middlewares
                 [http-kit               "2.2.0"]  ;; http server
                 [ring/ring-defaults     "0.2.1"]  ;; common middlewares
                 [bk/ring-gzip           "0.1.1"]
                 [ring/ring-json         "0.4.0"]
                 [ring.middleware.logger "0.5.0"]

                 ;;; api:
                 [metosin/compojure-api "1.1.9"]   ;; rest api with swagger docs
                 [liberator             "0.14.1"]  ;; correct http responses
                 [ataraxy               "0.4.0"]   ;; bidirectional routing

                 ;;; html:
                 ;;; TODO: add preferred libs

                 ;;; db:
                 [org.postgresql/postgresql      "42.1.1"]
                 [clj-factum/clientlib           "0.1.1-SNAPSHOT"]
                 [clj-factum/serverlib           "0.1.1-SNAPSHOT"]
                 [clj-factum/embedded-transport  "0.1.1-SNAPSHOT"]
                 [clj-factum/websocket-transport "0.1.1-SNAPSHOT"]
                 [clj-factum/postgres-backend    "0.1.1-SNAPSHOT"]

                 ;;; auth:
                 [buddy "1.3.0"]

                 ;;; date & time:
                 [clj-time                    "0.12.0"] ;; for clj
                 [com.andrewmcveigh/cljs-time "0.4.0"]  ;; for cljs

                 ;;; crypto:
                 [digest "1.4.5"]

                 ;;; logging:
                 [com.taoensso/timbre "4.4.0"]
                 ]

  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-environ   "1.1.0"]
            [lein-less "1.7.5"]]

  :source-paths ["src/clj" "src/cljc"]
  :java-source-paths ["src/java"]
  :target-path "target/%s"
  :clean-targets ^{:protect false} [:target-path :compile-path
                                    "resources/public/compiled"]

  :main ^:skip-aot {{project-name}}.core
  :uberjar-name "{{project-name}}.jar"

  :profiles {:dev {:dependencies [[figwheel "0.5.8"]
                                  [figwheel-sidecar "0.5.8"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.12"]]
                   :plugins [[lein-figwheel "0.5.8"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns repl
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :env {:platform-stage "dev"
                         :loglevel "debug"}}

             :uberjar {:prep-tasks ["compile" ["cljsbuild" "once" "browser-min"]]
                       :hooks [leiningen.less]
                       :omit-source true
                       :aot :all}}

  :cljsbuild {:builds
              [{:id "browser-dev"
                :source-paths ["src/cljs" "src/cljc"]
                :figwheel true
                :compiler {:output-to             "resources/public/compiled/js/{{project-name}}.js"
                           :output-dir            "resources/public/compiled/js"
                           :optimizations         :none
                           :source-map-timestamp  true
                           :print-input-delimiter true
                           :output-wrapper        true
                           :pretty-print          true}}

               {:id "browser-min"
                :source-paths ["src/cljs" "src/cljc"]
                :jar true
                :compiler {:output-to             "resources/public/compiled/js/{{project-name}}.js"
                           :output-dir            "target/browser-min"
                           :optimizations         :advanced
                           :externs               []}}]}

  :figwheel {:css-dirs ["resources/public/compiled/css"]
             :ring-handler repl/ring-handler
             :server-logfile "log/figwheel.log"}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/compiled/css"})
