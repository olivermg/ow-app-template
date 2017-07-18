(ns leiningen.new.ow-app
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             year project-name sanitize-ns]]
            [leiningen.core.main :as main]))

(def render (renderer "ow-app"))

(defn ow-app
  "FIXME: write documentation"
  [name & hints]
  (when (.startsWith name "+")
    (main/abort "Failed to create project: no project name specified."))
  (main/info (str "Generating a new ow-app project named " name "..."))
  (let [main-ns (sanitize-ns name)
        data {:raw-name name
              :name (project-name name)
              :namespace main-ns
              :dirs (name-to-path main-ns)
              :year (year)}]
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["CHANGELOG.md" (render "CHANGELOG.md" data)]
             ["LICENSE" (render "LICENSE" data)]
             [".gitignore" (render ".gitignore" data)]
             [".hgignore" (render ".hgignore" data)]
             ["dev/repl.clj" (render "dev/repl.clj" data)]
             ["resources/public/index.html" (render "resources/public/index.html" data)]
             "src/clj/{{dirs}}"
             "src/cljc/{{dirs}}"
             "src/cljs/{{dirs}}"
             "src/java/{{dirs}}"
             "src/less"
             "resources/public/css"
             "resources/public/js"
             "resources/public/fonts"
             "resources/public/img")))
