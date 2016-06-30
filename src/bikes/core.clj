;; Date: 30 June 2016
;; Author: Ryuei Sasaki
;; https://github.com/louixs

(ns bikes.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clj-time.core :as time]
            [clj-time.periodic :as p]
            ;; Emailing
            [postal.core :refer [send-message]]
            ;; immutant framework scheduling
            [immutant.scheduling :refer :all]              
            ;;referencing functions from other files in the project           
            [bikes.util :refer :all])
  (:use     ;; clj-xpath
            clj-xpath.core))

;; -----------------------
;; functions for email body

;; generic prepend title funciton for email bodies
(defn prepend-title [title text]
  (apply str "<br><br> --->> " title " <<--- <br><br>" text))

;; for formatting 
(defn append-br [text]
  (apply str text " <br> "))
(defn prepend-br [text]
  (apply str " <br> " text))
(defn prepend-bullet [text]
  (apply str "» " text))

;; ----------------------------
;; Santander cycle hire doc stations bike availablity status in London
;; Data provided by TFL
;; -----------------------------

;; URL for the realtime data in xml
(def bike-url "https://tfl.gov.uk/tfl/syndication/feeds/cycle-hire/livecyclehireupdates.xml")

;; Get and memoize data
(def bike-data
  (memoize #(slurp bike-url)))

;; convert xml to doc for efficiency using clj-xpath 
(def xmldoc
  (memoize #(xml->doc (bike-data))))

;; Using xpath query to obtain data
;; Specifying the bike station nodes with id
(defn bike-availability []
  (->> ($x "/stations/station[id=605 or id=43]" (xmldoc))
       (map 
        (fn [item]
          {:name ($x:text "./name" item)
           :seperator (str " » Bikes:")
           :nbBikes ($x:text "./nbBikes" item)
           ;:nbDocks ($x:text "./nbDocks" item)
}))))

;; Formatting data and converting to text
(def bike-body
  (->> (bike-availability)
       (map vals)
       (map #(str/join " "%))
       (map #(append-br %))
       (apply str)))

;; send email using postal
;; accepts subjects and body as arguments to make it easier to compose emails with different subjects
;; currently configured for gmail
;; you need to change the following to user your own gmail
;; :user - gmail user name such as your.name
;; :pass - password
;; :from - put your email address 
;; :to - recipients address - I made it so that I sent email from myself to my email


(defn send-email [subj body]    
   (send-message {:host "smtp.gmail.com"
                   :user "your.username"
                   :pass "your_password"
                   :ssl true}
                  {:from "your.gmail@gmail.com"
                   :to "your.gmail@gmail.com"
                   :subject subj
                   :body [{:type "text/html; charset=utf-8"
                           :content body}]}))

;; defining email with a subject
(defn send-email-subj [body] 
  (send-email "[UPDATE] Bike dock status" body))

(defn send-email-bike []
  (->> (prepend-title "Morning! Here's your status today. Bike safe! " bike-body)
       (send-email-subj)))

;; Scheduling jobs using Immutant
;; using cron syntax to send to send email at 6:50 AM on weekdays

(defn run-app []
  (do
    (println "LOG: Send santander Seymour dock availability every morning weekdays")
    (schedule send-email-bike (cron "6 50 1 ? * MON-FRI"))))

(defn -main []
  (run-app))
