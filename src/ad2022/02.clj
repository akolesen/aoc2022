(ns ad2022.02
  (:require [clojure.set :as set]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(def data
  (->> (io/reader (io/resource "02"))
       (line-seq)
       (map #(string/split % #" "))
       (map (partial map keyword))))

(def weaker {:rock     :scissors
             :scissors :paper
             :paper    :rock})
(def stronger (set/map-invert weaker))

(def decode-opp-bet {:A :rock
                     :B :paper
                     :C :scissors})

(def decode-my-bet {:X :rock
                    :Y :paper
                    :Z :scissors})

(def decode-my-result {:X :lose
                       :Y :draw
                       :Z :win})

(def costs {:rock     1
            :paper    2
            :scissors 3})

(defn decode-bet-bet
  [[opp-bet my-bet]]
  [(decode-opp-bet opp-bet) (decode-my-bet my-bet)])

(defn decode-bet-result
  [[opp-bet my-result]]
  (let [opp-bet   (decode-opp-bet opp-bet)
        my-result (decode-my-result my-result)
        my-bet    (case my-result
                    :lose (weaker opp-bet)
                    :draw opp-bet
                    :win  (stronger opp-bet))]
    [opp-bet my-bet]))

(defn reward
  [opp-bet my-bet]
  (cond
    (= my-bet (stronger opp-bet)) 6
    (= my-bet opp-bet)            3
    :else                         0))

(defn score
  [[opp-bet my-bet]]
  (+ (costs my-bet) (reward opp-bet my-bet)))

(defn game-result
  [data]
  (->> (map score data)
       (apply +)))

(defn part-one
  [data]
  (->> data
       (map decode-bet-bet)
       (game-result)))

(defn part-two
  [data]
  (->> data
       (map decode-bet-result)
       (game-result)))

(part-one data)                         ; 13446
(part-two data)                         ; 13509
