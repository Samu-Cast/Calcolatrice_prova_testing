const Calculator = require('../calculator/Calculator.js');

describe('Calculator', () => {
    test('add funziona correttamente', () => {
        const calc = new Calculator(10);
        calc.add(5);
        expect(calc.getValue()).toBe(15);
    });

    test('subtract funziona correttamente', () => {
        const calc = new Calculator(10);
        calc.subtract(3);
        expect(calc.getValue()).toBe(7);
    });

    test('multiply funziona correttamente', () => {
        const calc = new Calculator(5);
        calc.multiply(3);
        expect(calc.getValue()).toBe(15);
    });

    test('divide funziona correttamente', () => {
        const calc = new Calculator(20);
        calc.divide(4);
        expect(calc.getValue()).toBe(5);
    });

    test('divide per zero lancia errore', () => {
        const calc = new Calculator(10);
        expect(() => calc.divide(0)).toThrow('Impossibile dividere per zero');
    });
});
