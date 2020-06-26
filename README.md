# cont

A [delimited continuations](https://en.wikipedia.org/wiki/Delimited_continuation) library for Clojure.

## Usage

Non-determinism

```clojure
(require '[cont.core :refer [shift reset]])

(defn amb [& xs]
  (shift k (mapcat k xs)))

(reset
  (let [x (amb 1 2 3)
        y (amb 2 4 6)]
    (if (zero? (mod (+ x y) 3))
      [[x y]])))
;=> ([1 2] [2 4] [3 6])
```

Generator

```clojure
(defn yield [x]
  (shift k (cons x (k nil))))

(reset
  (yield 1)
  (yield 2)
  (yield 3))
;=> (1 2 3)
```

## License

Copyright © 2014 Sanshiro Yoshida.

Distributed under the Eclipse Public License.
