(defproject org.openvoxproject/kitchensink "3.5.6-SNAPSHOT"
  :description "Clojure utility functions"
  :url "https://github.com/openvoxproject/clj-kitchensink"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}

  :min-lein-version "2.9.1"

  ;; Abort when version ranges or version conflicts are detected in
  ;; dependencies. Also supports :warn to simply emit warnings.
  ;; requires lein 2.2.0+.
  :pedantic? :abort

  ;; Generally, try to keep version pins in :managed-dependencies and the libraries
  ;; this project actually uses in :dependencies, inheriting the version from
  ;; :managed-dependencies. This prevents endless version conflicts due to deps of deps.
  ;; Renovate should keep the versions largely in sync between projects.
  :managed-dependencies [[org.clojure/clojure "1.12.4"]
                         [org.clojure/tools.cli "1.3.250"]
                         [org.clojure/tools.logging "1.3.1"]
                         
                         [cheshire "5.13.0"]
                         [clj-commons/fs "1.6.312"]
                         [clj-time "0.15.2"]
                         [digest "1.4.10"]
                         [org.apache.commons/commons-compress "1.28.0"]
                         [org.ini4j/ini4j "0.5.4"]
                         [org.tcrawley/dynapath "1.1.0"]
                         [slingshot "0.12.2"]]

  :dependencies [[org.clojure/clojure]
                 [org.clojure/tools.cli]
                 [org.clojure/tools.logging]
                
                 [cheshire]
                 [digest]
                 [clj-commons/fs]
                 [clj-time]
                 [org.apache.commons/commons-compress]
                 [org.ini4j/ini4j]
                 [org.tcrawley/dynapath]
                 [slingshot]]

  ;; By declaring a classifier here and a corresponding profile below we'll get an additional jar
  ;; during `lein jar` that has all the code in the test/ directory. Downstream projects can then
  ;; depend on this test jar using a :classifier in their :dependencies to reuse the test utility
  ;; code that we have.
  :classifiers [["test" :testutils]]

  :profiles {:testutils {:source-paths ^:replace ["test"]}}

  ;; this plugin is used by jenkins jobs to interrogate the project version
  :plugins [[lein-project-version "0.1.0"]
            [jonase/eastwood "1.4.3" :exclusions [org.clojure/clojure]]]

  :eastwood {:ignored-faults {:unused-ret-vals {puppetlabs.kitchensink.classpath [{:line 93}]}
                              :deprecations {puppetlabs.kitchensink.classpath [{:line 66}
                                                                               {:line 91}]
                                             puppetlabs.kitchensink.core true
                                             puppetlabs.kitchensink.core-test true}
                              :reflection {puppetlabs.kitchensink.file [{:line 62}]
                                           puppetlabs.kitchensink.core [{:line 929}]}
                              :constant-test {puppetlabs.kitchensink.core-test [{:line 731}
                                                                                {:line 738}]}}
             :continue-on-exception true}

  :test-selectors {:default (complement :slow)
                   :slow :slow}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases false}]])
