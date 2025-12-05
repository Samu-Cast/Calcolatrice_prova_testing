const Calculator = require('../calculator/Calculator.js');

describe('Calculator - Test Base', () => {

    test('Addizione funziona', () => {
        const calc = new Calculator(10);
        calc.add(5);
        expect(calc.getValue()).toBe(15);
    });

    test('Sottrazione funziona', () => {
        const calc = new Calculator(10);
        calc.subtract(3);
        expect(calc.getValue()).toBe(7);
    });

    test('Moltiplicazione funziona', () => {
        const calc = new Calculator(5);
        calc.multiply(3);
        expect(calc.getValue()).toBe(15);
    });

    test('Divisione funziona', () => {
        const calc = new Calculator(20);
        calc.divide(4);
        expect(calc.getValue()).toBe(5);
    });

    test('Divisione per zero lancia errore', () => {
        const calc = new Calculator(10);
        expect(() => calc.divide(0)).toThrow('Impossibile dividere per zero');
    });
});
