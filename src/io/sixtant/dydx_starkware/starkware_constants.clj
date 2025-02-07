(ns io.sixtant.dydx-starkware.starkware-constants
  "Constants for dYdX cryptography and serialization.

  Starkware[1] teamed up with dYdX for v3 of their API, and they brought along
  a custom hashing function[2] and their own elliptic curve[3], as well as a
  custom ECDSA implementation based on RFC 6979.

  [1] https://starkware.co/
  [2] https://starkware.co/starkex/docs/pedersen.html
  [3] https://starkware.co/starkex/docs/starkcurve.html"
  (:require [taoensso.truss :as truss]))


(set! *warn-on-reflection* true)


;;; Message serialization/hashing constants


(def ^:const order-prefix 0x03)
(def ^:const order-padding-bits 0x11)

(def ^:const conditional-transfer-fee-asset-id 0x0)
(def ^:const conditional-transfer-max-fee 0x0)
(def ^:const conditional-transfer-prefix 0x05)
(def ^:const conditional-transfer-padding-bits 0x51)


;; We can inject these at compile time :)
(def ^:private order-field-bit-lengths
  {:asset-id-synthetic     128
   :asset-id-collateral    250
   :asset-id-fee           250
   :quantums-amount        64
   :nonce                  32
   :position-id            64
   :expiration-epoch-hours 32})


(def ^:private conditional-transfer-field-bit-lengths
  {:asset-id               250
   :receiver-public-key    251
   :position-id            64
   :condition              251
   :quantums-amount        64
   :nonce                  32
   :expiration-epoch-hours 32})


(defmacro obits
  "Resolve at compile time to the bit length for a stark order field."
  [field-id]
  (if (keyword? field-id) ; compile time reference
    (truss/have number? (order-field-bit-lengths field-id))
    (throw (IllegalArgumentException. "Expected compile-time field name."))))


(defmacro ctbits
  "Resolve at compile time to the bit length for a stark conditional transfer
  field."
  [field-id]
  (if (keyword? field-id) ; compile time reference
    (truss/have number? (conditional-transfer-field-bit-lengths field-id))
    (throw (IllegalArgumentException. "Expected compile-time field name."))))


(comment
  (macroexpand '(obits :nonce))
  ;=> 32
  )


;;; Elliptic curve constants


;; See [1] for Weierstrass specification of their curve, and [2] for some
;; reminders about how it all fits together.
;;
;; [1] https://starkware.co/starkex/docs/starkcurve.html
;; [2] https://crypto.stackexchange.com/questions/51350/what-is-the-relationship-between-p-prime-n-order-and-h-cofactor-of-an-ell


;; All points on the curve are (mod ec-prime)
(def ^:const ec-prime
  (biginteger ; = 2^251 + 17*2^192 + 1
    3618502788666131213697322783095070105623107215331596699973092056135872020481))


;; This curve can be represented in short Weierstrass form:
;;    y^2 = x^3 - alpha*x + beta
;; for a, b ∈ F(ec-prime) (where F(ec-prime) is 'the prime field')
(def ^:const ec-alpha BigInteger/ONE)
(def ^:const ec-beta
  (biginteger
    3141592653589793238462643383279502884197169399375105820974944592307816406665))


;; The EC point in affine (x, y) coordinates by which all other points on the
;; curve can be reached with scalar multiplication.
(def ^:const ec-generator-point-x
  (biginteger 874739451078007766457464989774322083649278607533249481151382481072868806602))
(def ^:const ec-generator-point-y
  (biginteger 152666792071518830868575557812948353041420400780739481342941381225525861407))


;; The curve order is an upper bound on the the number of different points in
;; the finite field that can be yielded by multiplying the generator point by
;; scalars. Small subgroup attacks happen when the order is significantly
;; not-prime, so the order of the curve is usually a prime multiplied by a small
;; co-factor, or simply a prime (i.e. cofactor = 1).
(def ^:const ec-order
  (biginteger
    3618502788666131213697322783095070105526743751716087489154079457884512865583))


;; Max bits for Pedersen hashing & ECDSA, derived from field prime
(def ^:const n-element-bits-ecsda (dec (.bitLength ec-prime)))
(def ^:const n-element-bits-hash (.bitLength ec-prime))

(def ^:const big-integer-one
  (biginteger
    1))

(def ^:const big-integer-two
  (biginteger
    2))
