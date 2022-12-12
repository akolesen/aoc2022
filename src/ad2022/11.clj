(ns ad2022.11
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn parse-monkey
  [data]
  (let [[_ items-line
         op-line
         test-line
         true-line
         false-line]     (string/split data #"\n")
        items            (mapv read-string (string/split (subs items-line 18) #", "))
        [op default-arg] (take-last 2 (string/split op-line #" "))
        op-fn            (fn [arg]
                           (let [op (eval (read-string op))]
                             (if (= default-arg "old")
                               (op arg arg)
                               (op (read-string default-arg) arg))))
        div-by           (read-string (last (string/split test-line #" ")))
        true-monkey-idx  (read-string (last (string/split true-line #" ")))
        false-monkey-idx (read-string (last (string/split false-line #" ")))
        next-fn          (fn [old modulus-fn]
                           (let [new-level ((comp modulus-fn op-fn) old)]
                             [(if (= 0 (mod new-level div-by))
                                true-monkey-idx
                                false-monkey-idx) new-level]))]
    {:items items :next-fn next-fn :n 0 :div-by div-by}))

(def data
  (let [data (slurp (io/resource "11"))]
    (->> (string/split data #"\n\n")
         (mapv parse-monkey))))

(defn item-turn
  [monkey modulus-fn next-fn state level]
  (let [[new-monkey new-level] (next-fn level modulus-fn)]
    (-> state
        (update-in [new-monkey :items] #(conj % new-level))
        (update-in [monkey :n] inc))))

(defn monkey-turn
  [modulus-fn state [monkey-idx {:keys [next-fn]}]]
  (let [item-turn-fn (partial item-turn monkey-idx modulus-fn next-fn)
        new-state    (reduce item-turn-fn state (get-in state [monkey-idx :items]))]
    (assoc-in new-state [monkey-idx :items] [])))

(defn play-round
  [modulus-fn state]
  (->> state
       (map-indexed vector)
       (reduce (partial monkey-turn modulus-fn) state)))

(defn run
  [state modulus-fn rounds]
  (->> (range rounds)
       (reduce (fn [state _] (play-round modulus-fn state)) state)
       (map :n)
       (sort)
       (take-last 2)
       (apply *)))

(defn part-one
  [data]
  (run data #(quot % 3) 20))

(defn gcd
  [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))

(defn lcm
  [a b]
  (/ (* a b) (gcd a b)))

(defn part-two
  [data]
  (let [lcm (reduce lcm (map :div-by data))]
    (run data #(mod % lcm) 10000)))

(part-one data)                         ;; 76728
(part-two data)                         ;; 21553910156
