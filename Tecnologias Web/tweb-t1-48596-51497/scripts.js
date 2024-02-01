// Variáveis para controle de paginação.
var farmaciasPorPagina = 5;
var paginaAtual = 1;

//Variável global para os gráficos.
let grafico;

// + 222 corresponde à class dos elementos da tag <p> dentro da <div id='lista-agendamentos'>
// + 333 corresponde ao id dos elementos da tag <div> dentro da <div id='+ 444'>
// + 444 corresponde ao id dos elementos da tag <div> dentro da <div id='farmacias-lista'>
// + 555 corresponde à class dos elementos da tag <p> dentro da <div id='+ 333'>
// + 666 corresponde à class dos elementos da tag <button> dentro da <div id='+ 555'>

// Função para exibir a lista de farmácias com base na página atual
function exibirFarmacias(data) {

    var farmacias = data.farmacias;
    var div1 = document.getElementById("farmacias-lista");
    var j = 0;

    // Calcular o total de páginas, se o número da divisão for decimal, arredonda para baixo.
    var totalPaginas = Math.ceil(data.farmacias.length / farmaciasPorPagina);

    // Verificar se este elemento já existe
    if (document.getElementById((paginaAtual - 1) + 444) === null) {
        // Se não existir, cria todos os elementos
        for (let i = 0; i < totalPaginas; i++) {
            var div2 = document.createElement("div");
            div2.id = i + 444;
            div1.appendChild(div2);
        }
        // Mudar a variavel para corresponder ao primeiro elemento criado
        div2 = document.getElementById(j + 444);

        for (let i = 0; i < farmacias.length; i++) {
            let farmacia = farmacias[i];
            let listItem = document.createElement("a");
            let div3 = document.createElement("div");
            div3.id = farmacia.entity_id + 333;
            listItem.id = farmacia.entity_id;
            listItem.style.display = "none";
            div3.style.display = "none";
            listItem.href = "#";
            listItem.innerHTML += "<strong>" + farmacia.name + "</strong> - " + farmacia.postal_code_locality + "<br>";
            listItem.addEventListener("click", function() {
                detalharFarmacia(farmacia.entity_id);
            });
            div3.appendChild(listItem);
            
            // Verificar se é para colocar a farmacia na pagina seguinte
            if ((i + j + 1) % 6 === 0) {
                j++;
                div2 = document.getElementById(j + 444);
                div2.appendChild(div3);
            } else {
                div2.appendChild(div3);
            }
        }
        for (let i = 0; i < farmaciasPorPagina; i++) {
            document.getElementById(farmacias[i].entity_id).style.display = "block";
            document.getElementById(farmacias[i].entity_id + 333).style.display = "block";
        }
    } else {
        var children = document.getElementById((paginaAtual - 1) + 444).childNodes;
        for (let i = 0; i < children.length; i++) {
            if (children[i].firstChild.style.display === "none") {
                children[i].firstChild.style.display = "block";
                children[i].style.display = "block";

            }
        }
        for (let i = 0; i < totalPaginas; i++) {
            children = document.getElementById(i + 444).childNodes;
            for (let j = 0; j < children.length; j++) {
                if (children[j].firstChild.style.display === "block" && i !== (paginaAtual - 1)) {
                    children[j].firstChild.style.display = "none";
                    children[j].style.display = "none";
                }
            }
        }
    }

    // Atualizar a navegação da página
    exibirPaginacao(data.farmacias.length);

}

// Função para exibir a navegação da página
function exibirPaginacao(totalFarmacias) {
    var totalPaginas = Math.ceil(totalFarmacias / farmaciasPorPagina);
    var paginacaoHTML = document.createElement("p");
    paginacaoHTML.innerHTML = "Páginas: ";

    // Criar um botão para cada página.
    for (var i = 1; i <= totalPaginas; i++) {
        let pagina = document.createElement("a");
        pagina.innerHTML = ""+ i +"";
        pagina.href = "#";
        if (i === paginaAtual) {
            pagina.className = "active";
        } else {
            pagina.className = "";
        }
        pagina.addEventListener("click", (function(pagina_i) {
            return function () {
                mudarPagina(pagina_i);
            };
        })(i));
        paginacaoHTML.append(pagina);
    }

    // Exibir a navegação na div com o id "paginacao"
    $("#paginacao").html(paginacaoHTML);
}

// Função para colocar os detalhes da farmácia na div correspondente ao id da mesma.
function detalharFarmacia(farmID) {

    // Get para obter os detalhes da farmácia.
    var get = $.get("https://magno.di.uevora.pt/tweb/t1/farmacia/get/" + farmID);

    get.done(function(data) {

        var div = document.getElementById(farmID + 333);

        // Se os detalhes da farmácia já estiverem visíveis, esconde-os quando se clica novamente na farmácia.
        if (document.getElementById(farmID + 555) && document.getElementById(farmID + 555).style.display === "block") {

            var aux = document.getElementById(farmID + 555);
            document.getElementById(farmID + 666).style.display = "none";
            document.getElementById(farmID + 555).style.display = "none";
        }

        // Se não existir uma div com o id da farmácia, cria uma e coloca os detalhes da mesma.
        if (document.getElementById(farmID + 555) === null) {
            var detalhes = document.createElement("p");
            detalhes.setAttribute("id", farmID + 555);
            detalhes.style.display = "block";
            detalhes.style.fontSize = "0.9rem";
            div.appendChild(detalhes);


            detalhes.innerHTML += "<strong>Email:</strong> " + data.farmacia.email + "<br>";
            detalhes.innerHTML += "<strong>Localidade:</strong> " + data.farmacia.postal_code_region + ", " + data.farmacia.postal_code_locality + "<br>";
            detalhes.innerHTML += "<strong>Rua:</strong> " + data.farmacia.street_name + "<br>";
            detalhes.innerHTML += "<strong>Código Postal:</strong> " + data.farmacia.postal_code_zone + "-" + data.farmacia.postal_code_sufix + "<br>";
            detalhes.innerHTML += "<strong>Telefone:</strong> " + data.farmacia.telephone + "<br>";
            detalhes.innerHTML += "<strong>Vacinas:</strong>";
            for (let i = 0; i < data.farmacia.services.length; i++) {
                detalhes.innerHTML += " " + data.farmacia.services[i];

                // Adicionar uma ',' entre todas as vacinas menos no fim.
                if (i < data.farmacia.services.length - 1) {
                    detalhes.innerHTML += ", ";
                }
            }

            // Se o campo do diretor não for undefined, coloca o nome do diretor da farmácia.
            if (data.farmacia.director !== undefined) {
                detalhes.innerHTML += "<br> <strong>Director:</strong> " + data.farmacia.director + "<br><br>";
            }
            // Se a div com o id da farmácia já existir, quando se clica na farmácia mostra-se os detalhes da mesma.
        } else {
            document.getElementById(farmID + 555).style.display = "block";
        }


        // Se a div com o id da farmácia que foi clicada já existir e estiver visível, esconde-a.
        if (aux && document.getElementById(farmID + 555).innerText === aux.innerText) {
            document.getElementById(farmID + 555).style.display = "none";
        }

        agendar(data.farmacia.entity_id);

    });
}

function searchFarm(input_id) {
    var input_name, input_locality, filtro, farmacia_a, a, name, filtro2;
    input_name = document.getElementById("search_name");
    input_locality = document.getElementById("search_locality");
    filtro = input_name.value.toUpperCase();
    filtro2 = input_locality.value.toUpperCase();
    farmacia_a = document.getElementById(paginaAtual - 1 + 444).getElementsByTagName("a");
    var todas = document.getElementById("farmacias-lista").getElementsByTagName("a");


    // Tornar todas as farmacias invisiveis
    for (let i = 0; i < todas.length; i++) {
        todas[i].parentElement.style.display = "none";
        todas[i].style.display = "none";
    }

    // Se o input estiver vazio, torna visivel as farmácias da pagina atual
    if (filtro === "" && filtro2 === "") {
        for (let i = 0; i < farmacia_a.length; i++) {
            farmacia_a[i].parentElement.style.display = "block";
            farmacia_a[i].style.display = "block";
        }
    }

    // Verificar qual dos inputs está a ser preenchido
    if (input_id === "search_name") {
        $.post("https://magno.di.uevora.pt/tweb/t1/farmacia/search", { name: filtro }).done(function(data) {
            // Iterar sobre os dados recebidos e atualizar a lista
            for (let i = 0; i < data.farmacias.length; i++) {
                if (document.getElementById("failed_search").style.display === "block") {
                    document.getElementById("failed_search").style.display = "none";
                }
                a = data.farmacias[i].name;
                name = a.toUpperCase();
                if (name.indexOf(filtro) > -1 && farmacia_a[i] !== undefined && filtro !== "") {
                    document.getElementById(data.farmacias[i].entity_id).parentElement.style.display = "block";
                    document.getElementById(data.farmacias[i].entity_id).style.display = "block";
                }
            }
            if (data.farmacias.length === 0 && document.getElementById("failed_search").style.display === "none") {
                document.getElementById("failed_search").style.display = "block";
            }
        });
    } else if (input_id === "search_locality") {
        $.post("https://magno.di.uevora.pt/tweb/t1/farmacia/search", { locality: filtro2 }).done(function(data) {
            // Iterar sobre os dados recebidos e atualizar a lista
            for (let i = 0; i < data.farmacias.length; i++) {
                if (document.getElementById("failed_search").style.display === "block") {
                    document.getElementById("failed_search").style.display = "none";
                }
                a = data.farmacias[i].postal_code_locality;
                name = a.toUpperCase();
                if (name.indexOf(filtro2) > -1 && farmacia_a[i] !== undefined && filtro !== "" !== null) {
                    document.getElementById(data.farmacias[i].entity_id).parentElement.style.display = "block";
                    document.getElementById(data.farmacias[i].entity_id).style.display = "block";
                }
            }
            if (data.farmacias.length === 0 && document.getElementById("failed_search").style.display === "none") {
                document.getElementById("failed_search").style.display = "block";
            }
        });
    }
}

function agendar(ID) {
    var div = document.getElementById(ID + 555);
    if (document.getElementById(ID + 666) === null) {
        // Cria o botao para a farmacia que tem o elemento de tag <p> como o id (ID + 555)
        var botao = document.createElement("button");
        botao.setAttribute("id", ID + 666);
        botao.onclick = function() {
            addAgendamentoForm(ID);
        };
        botao.innerHTML = "Agendar";
        if (document.getElementById(ID + 555).style.display === "block") {
            div.appendChild(botao);
        }
    } else {
        document.getElementById(ID + 666).style.display = "flex";
    }
}

function addAgendamentoForm(ID) {
    var div = document.getElementById(ID + 555);
    var form = document.getElementById("myForm1");
    // Se ele tiver invisivel, torna visivel
    if (form.style.display === "none" && div !== null) {
        div.appendChild(form);
        form.style.display = "block";
    } else {
        form.style.display = "none";
        for (let i = 0; i < document.getElementById(paginaAtual - 1 + 444).children.length; i++) {
            let detalhe = document.getElementById(paginaAtual - 1 + 444).children[i].getElementsByTagName("p")[0];
            // Se o que é pressionado for diferente do que já tá visivel, torna-o invisivel e torna visivel o que foi pressionado 
            if (detalhe !== undefined && detalhe.getElementsByTagName("form")[0] !== undefined && ID + 555 != detalhe.id) {
                detalhe.getElementsByTagName("form")[0].style.display = "none";
                if(div.getElementsByTagName("form") !== undefined) {
                    div.appendChild(form);
                }
                form.style.display = "block";
                document.getElementById("sucesso").style.display = "none";
            }
        }
    }
}

// Função para adicionar um agendamento de vacinação.
function addAgendamento() {
    var data_agend, id_user, vacina, gripe, covid, id_farm;

    event.preventDefault();

    //Obtém os valores dos inputs do formulário.
    data_agend = document.getElementById("data_agendamento").value;
    id_user = document.getElementById("user").value;
    id_farm = document.getElementById("myForm1").parentElement.id - 555;
    gripe = document.getElementById("gripe").checked;
    covid = document.getElementById("covid").checked;

    //Verifica se pelo menos um radio button está selecionado.
    if (gripe) {
        vacina = document.getElementById("gripe").value;
    } else {
        vacina = document.getElementById("covid").value;
    }

    //Se não estiver selecionado nenhum radio button, alerta o utilizador e impede o envio do formulário.
    if (!gripe && !covid) {
        alert('Tem de selecionar uma das vacinas para prosseguir com o envio do formulário.');
        return false;
    }

    //Envia os dados do agendamento obtidos pelo formulário.
    $.post("https://magno.di.uevora.pt/tweb/t1/schedule/add", {
            schedule_date: data_agend,
            user_id: id_user,
            entity_id: id_farm,
            vaccine: vacina
        })
        .done(function(data) {
            if (data.status === "ok") {
                document.getElementById("sucesso").style.display = "block";
            } else {
                document.getElementById("failed").style.display = "block";
            }
        });

}

function listarAgendamentos() {

    event.preventDefault();

    var id_user;
    id_user = document.getElementById("user_agendamentos").value;

    $.post("https://magno.di.uevora.pt/tweb/t1/schedule/list", {
        user_id: id_user
    }).done(function(data) {

        var form = document.getElementById("myForm2");

        //For para limpar o form.
        for (var i = 0; i < form.elements.length; i++) {
            var element = form.elements[i];

            // Verifica se o elemento é um input e não é o botão de submit e apaga tudo.
            if (element.tagName === 'INPUT' && element.type !== 'submit') {
                // Se for, dá reset
                element.value = '';
            }
        }

        var div_agendamentos = document.getElementById("lista-agendamentos");


        if (document.getElementsByClassName(id_user + 222).length === 0 && data.schedule_list.length > 0) {
            for (let i = 0; i < data.schedule_list.length; i++) {
                var p = document.createElement("p");
                p.className = data.schedule_list[i][0] + "222";
                p.id = data.schedule_list[i][4];
                let name = document.getElementById(data.schedule_list[i][1] + 333).firstChild.innerText;
                p.innerHTML = "<strong>Nome:</strong> " + name.substring(0, name.indexOf("-")) + "<br>";
                p.innerHTML += "<strong>Vacina:</strong> " + data.schedule_list[i][2] + "<br>";
                p.innerHTML += "<strong>Data:</strong> " + data.schedule_list[i][3] + "<br>";
                var button_agend = document.createElement("button");
                button_agend.innerHTML = "Remover";
                button_agend.onclick = function() {
                    removeAgendamentos(data.schedule_list[i][0] + "222", data.schedule_list[i][4]);
                }
                p.appendChild(button_agend);
                div_agendamentos.appendChild(p);
                for (let i = 0; i < div_agendamentos.children.length; i++) {
                    if (div_agendamentos.children[i].className !== (id_user + 222)) {
                        div_agendamentos.children[i].style.display = "none";
                    }
                }
            }
        } else if (data.schedule_list.length === 0) {
            for (let i = 0; i < div_agendamentos.children.length; i++) {
                div_agendamentos.children[i].style.display = "none";
            }
        } else {
            for (let i = 0; i < div_agendamentos.children.length; i++) {
                if (div_agendamentos.children[i].className !== (id_user + 222)) {
                    div_agendamentos.children[i].style.display = "none";
                } else {
                    div_agendamentos.children[i].style.display = "block";
                }
            }
        }
    })
}

//Função para remover um agendamento de vacinação.
function removeAgendamentos(id_user, code_agend) {

    //Envia o id do agendamento a remover utilizando o id do utilizador e o código do agendamento.
    $.post("https://magno.di.uevora.pt/tweb/t1/schedule/remove", { schedule_code: code_agend, user_id: id_user.replace("222", "") }).done(function(data) {

        if (data.status === "ok") {
            document.getElementById(code_agend).remove();
        }

    })
}

// Função para mudar para uma página específica
function mudarPagina(pagina) {

    if (document.getElementById("search_name").value.length !== 0) {
        document.getElementById("search_name").value = "";
    }
    if (document.getElementById("search_locality").value.length !== 0) {
        document.getElementById("search_locality").value = "";
    }
    if (document.getElementById("failed_search").style.display === "block") {
        document.getElementById("failed_search").style.display = "none";
    }
    for (let i = 0; i < document.getElementById(paginaAtual - 1 + 444).children.length; i++) {
        let detalhe = document.getElementById(paginaAtual - 1 + 444).children[i].getElementsByTagName("p")[0];
        if (detalhe !== undefined && detalhe.style.display === "block") {
            detalhe.style.display = "none";
        }
        if (detalhe !== undefined && detalhe.getElementsByTagName("form")[0] !== undefined && detalhe.getElementsByTagName("form")[0].style.display === "block") {
            document.getElementById("sucesso").style.display = "none";
            detalhe.getElementsByTagName("form")[0].style.display = "none";
        }
    }
    paginaAtual = pagina;

    // Apenas quando a página for alterada
    inicio();
};

function inicio() {
    $.get("https://magno.di.uevora.pt/tweb/t1/farmacia/list", { page: paginaAtual }).done(function(data) {
        if (data.status === "ok") {
            // Exibir as farmácias para a página atual
            exibirFarmacias(data);
        } else {
            alert("Erro ao obter a lista de farmácias. Aviso: " + data.aviso);
        }
    })
}

// Função para listar as farmácias com vacinação para a gripe ou covid-19 dependendo do link que recebe.
function listar(link) {
    $.get(link, {})
        .done(function(data) {

                //Recebe a informação do servidor e guarda-a numa variável.
                const listaFarmacias = data.farmacias;
                var element = document.getElementById("farmacia-titulo");

                //If para mudar o título da página dependendo do link que recebe.
                if (link == 'https://magno.di.uevora.pt/tweb/t1/program/gripe/list') {
                    element.innerHTML = "FARMÁCIAS GRIPE"

                } else element.innerHTML = "FARMÁCIAS COVID19"


                document.getElementById('myChart').style.display = 'block';
                document.getElementById('myChart').parentElement.style.display = 'flex';
                desenhaGrafico(link);

                //Limpa a lista de farmácias.
                const ulElement = $("#farmacias-lista-vacinas");
                ulElement.empty();

                //Percorre as farmácias e adiciona cada uma à lista.
                listaFarmacias.forEach(farmacia => {

                    const li = $("<li>").text(`${farmacia.name} - ${farmacia.postal_code_locality}`);
                    ulElement.append(li);
                });
            }

        )
        .fail(function() {
            console.error('Erro ao obter os dados das farmácias.');
        });


    if (document.getElementById('lista-farmacias').style.display == 'none') {
        document.getElementById('lista-farmacias').style.display = 'block';
    }

    //Esconder os forms.
    document.getElementById('myFormRemove').style.display = 'none';
    document.getElementById('h2FormRemove').style.display = 'none';
    document.getElementById('myForm').style.display = 'none';
    document.getElementById('h2Form').style.display = 'none';

}

//Função para desenhar o gráfico.
function desenhaGrafico(link) {

    //Recebe a informação do servidor.
    $.get(link, {})
        .done(function(data) {

                var farm_evora = 0;
                var farm_lisboa = 0;
                var farm_paio = 0;
                var farm_sacavem = 0;
                var farm_ramada = 0;
                var farm_portoS = 0;
                const listaFarmacias = data.farmacias;

                //Procura todas as farmácias e soma cada vez que se repete uma região.
                listaFarmacias.forEach(farmacia => {
                    if (farmacia.postal_code_locality == 'ÉVORA') farm_evora++;
                    if (farmacia.postal_code_locality == 'LISBOA') farm_lisboa++;
                    if (farmacia.postal_code_locality == 'Sacavém') farm_sacavem++;
                    if (farmacia.postal_code_locality == 'RAMADA') farm_ramada++;
                    if (farmacia.postal_code_locality == 'ALDEIA DE PAIO PIRES') farm_paio++;
                    else if (farmacia.postal_code_locality == 'PORTO SALVO') farm_portoS++;
                });

                //If para desenhar o gráfico dependendo do link que recebe.
                if (link == 'https://magno.di.uevora.pt/tweb/t1/program/gripe/list') {
                    var data = {
                        labels: ['Évora', 'Lisboa', 'Sacavém', 'Porto Salvo', 'Aldeia de Paio Pires'],
                        datasets: [{
                            label: 'Número de Farmácias',
                            data: [farm_evora, farm_lisboa, farm_sacavem, farm_portoS, farm_paio],
                            backgroundColor: 'rgba(88, 125, 69, 0.5)',
                            borderColor: 'rgba(55, 126, 34, 10)',
                            borderWidth: 1
                        }]

                    }

                } else {
                    var data = {
                        labels: ['Évora', 'Lisboa', 'Sacavém', 'Ramada', 'Aldeia de Paio Pires'],
                        datasets: [{
                            label: 'Número de Farmácias',
                            data: [farm_evora, farm_lisboa, farm_sacavem, farm_ramada, farm_paio],
                            backgroundColor: 'rgba(88, 125, 69, 0.5)',
                            borderColor: 'rgba(55, 126, 34, 10)',
                            borderWidth: 1
                        }]
                    }
                }

                //Se já existir um gráfico, apaga-o.
                if (grafico) {
                    grafico.destroy();
                    grafico = null; // Reseta o gráfico.
                }

                //Constrói o gráfico
                var ctx = document.getElementById('myChart').getContext('2d');
                grafico = new Chart(ctx, {
                    type: 'bar',
                    data: data,
                    options: {
                        scales: {
                            y: {
                                ticks: {
                                    beginAtZero: true,
                                    precision: 0,
                                }
                            }

                        }
                    }
                });
            }


        )
        .fail(function() {
            console.error('Erro ao obter os dados das farmácias.');
        });


}

//Função para apagar o gráfico e o conteúdo da página.
function apagarConteudo() {

    //Se estiver o gráfico, remove-o.
    if (grafico) {
        grafico.destroy();
        document.getElementById('myChart').parentElement.style.display = 'none';
        document.getElementById('myChart').style.display = 'none';
        grafico = null; // Reseta o gráfico.
    }

    //Apaga o resto do conteúdo presente.
    document.getElementById('lista-farmacias').style.display = 'none';
}


//Função para mostrar o formulário Adicionar.
function mostrarFormAdicionar() {

    //Apaga o conteúdo anterior.
    apagarConteudo();
    document.getElementById('myChart').parentElement.style.display = 'none';
    document.getElementById('myChart').style.display = 'none';

    document.getElementById('myFormRemove').style.display = 'none';
    document.getElementById('h2FormRemove').style.display = 'none';

    //Coloca o conteúdo do formulário.
    document.getElementById('myForm').style.display = 'block';
    document.getElementById('h2Form').style.display = 'block';
}
//Função para mostrar o formulário Remover.
function mostrarFormRemover() {

    //Apaga o conteúdo anterior.
    apagarConteudo();
    document.getElementById('myChart').parentElement.style.display = 'none';
    document.getElementById('myChart').style.display = 'none';

    document.getElementById('myForm').style.display = 'none';
    document.getElementById('h2Form').style.display = 'none';

    //Coloca o conteúdo do formulário
    document.getElementById('myFormRemove').style.display = 'block';
    document.getElementById('h2FormRemove').style.display = 'block';
}
//Função para controlar o envio do formulário Adicionar.
function submeterFormAdicionar() {

    //Remove o comportamento default de quando se submete um formulário.
    event.preventDefault();
    var nomeFarm, localFarm, idFarm, vacina1, vacina2;

    //Obtém os valores dos inputs do formulário.
    nomeFarm = document.getElementById('nomeFarm').value;
    localFarm = document.getElementById('localFarm').value
    idFarm = document.getElementById('idFarmacia').value;
    vacina1 = document.querySelector('.vacina1Checkbox').checked;
    vacina2 = document.querySelector('.vacina2Checkbox').checked;


    //Verifica se pelo menos uma checkbox está selecionada.
    if (vacina1 && vacina2) {
        vacina1 = 'covid-19';
        vacina2 = 'gripe';
    } else if (vacina1 && !vacina2) {
        vacina1 = 'covid-19'
        vacina2 = '';
    } else if (!vacina1 && vacina2) {
        vacina1 = 'gripe';
        vacina2 = '';

        //Se nenhuma checkbox tiver selecionada, alerta o utilizador e impede o envio do formulário.
    } else {
        alert('Tem de selecionar uma das vacinas para prosseguir com o envio do formulário.');
        return false;
    }

    var form = document.getElementById('myForm');

    //Passa por todos os elementos do form.
    for (var i = 0; i < form.elements.length; i++) {
        var element = form.elements[i];

        // Verifica se o elemento é um input e não é o botão de submit e apaga tudo.
        if (element.tagName === 'INPUT' && element.type !== 'submit') {
            var check1 = document.getElementById('gripe');
            var check2 = document.getElementById('covid-19');

            check1.checked = false;
            check2.checked = false;
            // Se for, dá reset.
            element.value = '';

        }
    }

    //Envia os dados da farmácia a adicionar obtida pelo formulário.
    $.post("https://magno.di.uevora.pt/tweb/t1/program/add/", {

        services: vacina1,
        vacina2,
        entity_id: idFarm,
        name: nomeFarm,
        local: localFarm

    }).done(function(data) {
        alert('Farmácia adicionada com sucesso!');
    }).fail(function(error) {
        console.error("Error:", error);
    });

}
//Função para controlar o envio do formulário Remover.
function submeterFormRemover() {
    event.preventDefault();
    var idFarm = document.getElementById('idFarmaciaRemove').value;


    //Envia o id da farmácia a remover obtida pelo formulário.
    $.post("https://magno.di.uevora.pt/tweb/t1/program/remove", {
        entity_id: idFarm
    }).done(function(data) {
        alert('Farmácia removida com sucesso!');
    })
}