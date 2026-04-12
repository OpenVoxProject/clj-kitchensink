(ns puppetlabs.kitchensink.json
  "Cheshire related functions

  This front-ends the common set of core cheshire functions:

  * generate-string
  * generate-stream
  * parse-string
  * parse-stream

  This namespace when 'required' will also setup some common JSON encoders
  globally, so you can avoid doing this for each call."
  (:require
    [cheshire.core :as core]
    [cheshire.generate :as generate]
    [clojure.java.io :as io]
    [clojure.tools.logging :as log])
  (:import
    com.fasterxml.jackson.core.JsonGenerator
    (java.time Instant LocalDate LocalDateTime)))


(defn- java-instant-encoder
  [^Instant data jsonGenerator]
  (.writeString ^JsonGenerator jsonGenerator ^String (.toString data)))

(def ^:dynamic *instant-encoder* java-instant-encoder)

(defn- java-localdate-encoder
  [^LocalDate data jsonGenerator]
  (.writeString ^JsonGenerator jsonGenerator ^String (.toString data)))

(def ^:dynamic *localdate-encoder* java-localdate-encoder)

(defn- java-localdatetime-encoder
  [^LocalDateTime data jsonGenerator]
  (.writeString ^JsonGenerator jsonGenerator ^String (.toString data)))

(def ^:dynamic *localdatetime-encoder* java-localdatetime-encoder)

(defn add-common-json-encoders!*
  "Non-memoize version of add-common-json-encoders!"
  []
  (when (satisfies? generate/JSONable (Instant/now))
    (log/warn "Overriding existing JSONable protocol implementation for java.time.Instant"))
  (when (satisfies? generate/JSONable (LocalDateTime/now))
    (log/warn "Overriding existing JSONable protocol implementation for java.time.LocalDateTime"))
  (when (satisfies? generate/JSONable (LocalDate/now))
    (log/warn "Overriding existing JSONable protocol implementation for java.time.LocalDate"))
  (generate/add-encoder
    java.time.Instant
    (fn [data jsonGenerator]
      (*instant-encoder* data jsonGenerator)))
  (generate/add-encoder
    java.time.LocalDate
    (fn [data jsonGenerator]
      (*localdate-encoder* data jsonGenerator)))
  (generate/add-encoder
    java.time.LocalDateTime
    (fn [data jsonGenerator]
      (*localdatetime-encoder* data jsonGenerator))))

(def
  ^{:doc "Registers some common encoders for cheshire JSON encoding.

  This is a memoize function, to avoid unnecessary calls to add-encoder.

  Ideally this function should be called once in your app, for example your
  main class.

  Encoders currently include:

  * java.time.Instant
  * java.time.LocalDate
  * java.time.LocalDateTime"}
  add-common-json-encoders! (memoize add-common-json-encoders!*))


(def default-pretty-opts {:date-format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" :pretty true})

(def ^String generate-string core/generate-string)

(def ^String generate-stream core/generate-stream)

(defn generate-pretty-string
  "Thinly wraps cheshire.core/generate-string, adding pretty printing
  from `default-pretty-opts`"
  ([obj]
     (generate-pretty-string obj default-pretty-opts))
  ([obj opts]
     (generate-string obj (merge default-pretty-opts opts))))

(defn generate-pretty-stream
  "Thinly wraps cheshire.core/generate-stream, adding pretty printing
  from `default-pretty-opts`"
  ([obj writer]
     (generate-pretty-stream obj writer default-pretty-opts))
  ([obj writer opts]
     (generate-stream obj writer (merge default-pretty-opts opts))))

(def parse-string core/parse-string)

(def parse-stream core/parse-stream)

(defn spit-json
  "Similar to clojure.core/spit, but writes the Clojure
   datastructure as JSON to `f`"
  [f obj & options]
  (with-open [writer ^java.io.BufferedWriter (apply io/writer f options)]
    (generate-pretty-stream obj writer))
  nil)
