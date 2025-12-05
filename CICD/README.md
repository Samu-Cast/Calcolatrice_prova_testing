# Calculator CI/CD

Esempio minimale di Continuous Integration con GitHub Actions.

## 📁 Struttura

```
.
├── Calculator.js          # Classe Calculator
├── Calculator.test.js     # Test con Jest
├── package.json           # Dipendenze
├── .github/
│   └── workflows/
│       └── ci.yml        # GitHub Actions CI
└── .gitignore
```

## 🧪 Testare in locale

```bash
# Installa dipendenze
npm install

# Esegui i test
npm test
```

## 🚀 Come funziona la CI

1. **Quando**: La CI parte automaticamente quando fai push o apri una PR sul branch `main`
2. **Cosa fa**: 
   - Installa Node.js 20
   - Installa le dipendenze (`npm install`)
   - Esegue i test (`npm test`)
3. **Dove**: Vai su GitHub → tab "Actions" per vedere i risultati

## 📤 Setup su GitHub

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/tuo-username/calculator-ci.git
git push -u origin main
```

Dopo il push, vai su GitHub nella sezione **Actions** per vedere la CI in esecuzione! ✅
