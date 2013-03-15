(defproject unwatch "0.1.0-SNAPSHOT"
  :description "unwatch jira"
  :url "https://github.com/doremi/unwatch"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.atlassian.jira/jira-rest-java-client "0.6-m9"]
                 [com.sun.jersey/jersey-json "1.16"]]
  :plugins [[lein-swank "1.4.4"]]
  :repositories [["jira" "https://maven.atlassian.com/public/"]
                 ["soap" "https://maven.atlassian.com/content/repositories/atlassian-contrib/"]]
  :main unwatch.core)
