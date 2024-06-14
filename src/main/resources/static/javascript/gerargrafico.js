const ctx = document.getElementById('graficoVendas');

let labelsX = ["NOV", "DEZ", "JAN", "FEV", "MAR", "ABR"];
let valores = [];

new Chart(ctx, {
    type: 'line',
    data: {
    labels: labelsX,
    datasets: [{
        label: 'Valor em R$ de Vendas por Período',
        data: valores,
        borderWidth: 1
    }]
    },
    options: {
    title: {
        display: true,
        fontSize: 20,
        text: "Relatório de Vendas"
    },
    labels: {
    fontStyle: "bold"
    },
    scales: {
        y: {
        beginAtZero: true
        }
    }
    }
});