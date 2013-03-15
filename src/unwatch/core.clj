(ns unwatch.core
  (:import
   (java.net URI)
   (com.atlassian.jira.rest.client.internal.jersey JerseyJiraRestClientFactory)
   (com.atlassian.jira.rest.client JiraRestClient IssueRestClient SearchRestClient NullProgressMonitor)
   (org.codehaus.jettison.json JSONObject))
  (:gen-class))

(def jiraURL "http://jira.xxx.com.tw:8080");
(def factory (new JerseyJiraRestClientFactory))
(def jiraServerUri (new URI jiraURL))
(def pm (NullProgressMonitor.))

(defn login-jira [username password]
  (let [restClient (. factory createWithBasicHttpAuthentication jiraServerUri username password)
        issueClient (. restClient getIssueClient)
        searchClient (. restClient getSearchClient)]
    {:issue-client issueClient :search-client searchClient}))

(defn search-noneed-watch-issue [session project username]
  (let [jql (str "project = " project " and watcher = " username " and assignee != " username)
        myissue-basic (. (session :search-client) searchJql jql pm)
        myissue (. myissue-basic getIssues)]
    myissue))

(defn unwatch-issues [session issues]
  (println "\nUnwatch" (count issues) "issues...\n")
  (doseq [issue issues]
    (let [issueClient (session :issue-client)
          key (. issue getKey)
          uri (.. (. issueClient getIssue key pm) getWatchers getSelf)]
      (println "Unwatching" key)
      (. issueClient unwatch uri pm)))
  (println "\nDone.\n"))

(defn prompt-read [prompt & hidden-input]
  (print prompt)
  (flush)
  (if hidden-input
    (String/valueOf (.readPassword (System/console) "" nil))
    (read-line)))

(defn -main [& args]
  (if (not= (count args) 1)
    (do
      (println "No project specified!")
      (println "Example: java -jar unwatch.jar MYPROJECT"))
    (do
      (let [username (prompt-read "[jira] Username: ")
            password (prompt-read "[jira] Password: " true)
            project (first args)
            session (login-jira username password)
            issues (search-noneed-watch-issue session project username)]
        (unwatch-issues session issues)))))
