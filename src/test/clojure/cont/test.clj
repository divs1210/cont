(ns cont.test
  (:require [clojure.test :refer :all]
            [cont.core :refer [shift reset]]))

(with-test
  (defn amb [& xs]
    (shift k (mapcat k xs)))
  (is (= (reset
           (let [x (amb 1 2 3)
                 y (amb 2 4 6)]
             (if (zero? (mod (+ x y) 3))
               [[x y]])))
         [[1 2] [2 4] [3 6]])))

(with-test
  (defn yield [x]
    (shift k (cons x (k nil))))
  (is (= (reset
           (yield 1)
           (yield 2)
           (yield 3)
           nil)
         [1 2 3])))

(with-test
  (def tick
    (shift k (fn [n] ((k nil) (inc n)))))
  (is (= ((reset
            tick
            tick
            identity)
          0)
         2)))

(deftest transformation
  (testing "loop"
    (is (= (reset (loop [x 0] x)) 0)))
  (testing "letfn"
    (is (= (reset (letfn [(f [x] x)] (f 0))) 0)))
  (testing "throw"
    (is (thrown? Exception (reset (throw (Exception.))))))
  (testing "."
    (is (= (reset (Integer/parseInt "0")) 0))
    (is (= (reset (.toString 0)) "0")))
  (testing "new"
    (is (= (reset (new String "0")) "0"))))