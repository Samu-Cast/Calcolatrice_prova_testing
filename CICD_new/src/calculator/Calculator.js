/**
 * Classe Calculator - Implementa una calcolatrice con operazioni base
 */
class Calculator {
    /**
     * Costruttore della calcolatrice
     * @param {number} initialValue - Valore iniziale (default: 0)
     */
    constructor(initialValue = 0) {
        this.currentValue = initialValue;
        this.previousValue = null;
        this.operation = null;
        this.history = [];
    }

    /**
     * Ottiene il valore corrente
     * @returns {number} Il valore corrente
     */
    getValue() {
        return this.currentValue;
    }

    /**
     * Imposta un nuovo valore
     * @param {number} value - Il nuovo valore
     */
    setValue(value) {
        if (typeof value !== 'number') {
            throw new Error('Il valore deve essere un numero');
        }
        this.currentValue = value;
    }

    /**
     * Addizione
     * @param {number} value - Il valore da aggiungere
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    add(value) {
        if (typeof value !== 'number') {
            throw new Error('Il valore deve essere un numero');
        }
        this.previousValue = this.currentValue;
        this.currentValue += value;
        this._addToHistory('add', value);
        return this;
    }

    /**
     * Sottrazione
     * @param {number} value - Il valore da sottrarre
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    subtract(value) {
        if (typeof value !== 'number') {
            throw new Error('Il valore deve essere un numero');
        }
        this.previousValue = this.currentValue;
        this.currentValue -= value;
        this._addToHistory('subtract', value);
        return this;
    }

    /**
     * Moltiplicazione
     * @param {number} value - Il valore per cui moltiplicare
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    multiply(value) {
        if (typeof value !== 'number') {
            throw new Error('Il valore deve essere un numero');
        }
        this.previousValue = this.currentValue;
        this.currentValue *= value;
        this._addToHistory('multiply', value);
        return this;
    }

    /**
     * Divisione
     * @param {number} value - Il valore per cui dividere
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    divide(value) {
        if (typeof value !== 'number') {
            throw new Error('Il valore deve essere un numero');
        }
        if (value === 0) {
            throw new Error('Impossibile dividere per zero');
        }
        this.previousValue = this.currentValue;
        this.currentValue /= value;
        this._addToHistory('divide', value);
        return this;
    }

    /**
     * Potenza
     * @param {number} exponent - L'esponente
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    power(exponent) {
        if (typeof exponent !== 'number') {
            throw new Error('L\'esponente deve essere un numero');
        }
        this.previousValue = this.currentValue;
        this.currentValue = Math.pow(this.currentValue, exponent);
        this._addToHistory('power', exponent);
        return this;
    }

    /**
     * Radice quadrata
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    sqrt() {
        if (this.currentValue < 0) {
            throw new Error('Impossibile calcolare la radice quadrata di un numero negativo');
        }
        this.previousValue = this.currentValue;
        this.currentValue = Math.sqrt(this.currentValue);
        this._addToHistory('sqrt', null);
        return this;
    }

    /**
     * Percentuale
     * @param {number} percentage - La percentuale da calcolare
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    percentage(percentage) {
        if (typeof percentage !== 'number') {
            throw new Error('La percentuale deve essere un numero');
        }
        this.previousValue = this.currentValue;
        this.currentValue = (this.currentValue * percentage) / 100;
        this._addToHistory('percentage', percentage);
        return this;
    }

    /**
     * Reset della calcolatrice
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    clear() {
        this.currentValue = 0;
        this.previousValue = null;
        this.operation = null;
        return this;
    }

    /**
     * Ripristina l'ultimo valore
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    undo() {
        if (this.previousValue !== null) {
            this.currentValue = this.previousValue;
            this.previousValue = null;
        }
        return this;
    }

    /**
     * Ottiene lo storico delle operazioni
     * @returns {Array} Array con tutte le operazioni eseguite
     */
    getHistory() {
        return [...this.history];
    }

    /**
     * Cancella lo storico
     * @returns {Calculator} Ritorna l'istanza per il chaining
     */
    clearHistory() {
        this.history = [];
        return this;
    }

    /**
     * Aggiunge un'operazione allo storico (metodo privato)
     * @private
     * @param {string} operation - Il tipo di operazione
     * @param {number|null} value - Il valore utilizzato
     */
    _addToHistory(operation, value) {
        this.history.push({
            operation,
            value,
            result: this.currentValue,
            timestamp: new Date()
        });
    }

    /**
     * Rappresentazione testuale della calcolatrice
     * @returns {string} Stringa rappresentativa
     */
    toString() {
        return `Calculator [current: ${this.currentValue}]`;
    }
}

// Export per uso in Node.js o moduli ES6
if (typeof module !== 'undefined' && module.exports) {
    module.exports = Calculator;
}
