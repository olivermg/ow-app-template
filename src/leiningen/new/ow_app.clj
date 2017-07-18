(ns leiningen.new.ow-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             year project-name sanitize-ns]]
            [leiningen.core.main :as main]
            [clojure.string :as str]))

(def render (renderer "ow-app"))

(defn ow-app
  "FIXME: write documentation"
  [name & hints]
  (when (.startsWith name "+")
    (main/abort "Failed to create project: no project name specified."))
  (main/info (str "Generating a new ow-app project named " name "..."))
  (let [main-ns (sanitize-ns name)
        pname (project-name name)
        shortname (-> pname
                      (str/split #"\.")
                      last)
        data {:raw-name name
              :name pname
              :namespace main-ns
              :dirs (name-to-path main-ns)
              :shortname shortname
              :year (year)}]
    (println data)
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["CHANGELOG.md" (render "CHANGELOG.md" data)]
             ["LICENSE" (render "LICENSE" data)]
             [".gitignore" (render ".gitignore" data)]
             [".hgignore" (render ".hgignore" data)]
             ["dev/repl.clj" (render "dev/repl.clj" data)]
             ["resources/public/index.html" (render "resources/public/index.html" data)]
             ["src/clj/{{dirs}}/core.clj" (render "src/clj/core.clj" data)]
             ["src/cljc/{{dirs}}/core.cljc" (render "src/cljc/core.cljc" data)]
             ["src/cljs/{{dirs}}/core.cljs" (render "src/cljs/core.cljs" data)]
             ["src/less/main.less" (render "src/less/main.less" data)]
             ["src/java/{{dirs}}/.gitkeep" (render ".gitkeep" data)]
             ["resources/public/css/.gitkeep" (render ".gitkeep" data)]
             ["resources/public/js/.gitkeep" (render ".gitkeep" data)]
             ["resources/public/fonts/.gitkeep" (render ".gitkeep" data)]
             ["resources/public/img/.gitkeep" (render ".gitkeep" data)])))
