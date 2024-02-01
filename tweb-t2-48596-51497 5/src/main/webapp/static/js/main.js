//Vai buscar os valores necessarios para o pagamento e lista
function getMbReference(value, event_id) {

    $.post("https://magno.di.uevora.pt/tweb/t2/mbref4payment", { amount: value }).done(function(data) {

        document.getElementById("entidade").value = data.mb_entity;
        document.getElementById("referencia").value = data.mb_reference;
        document.getElementById("valor").value = data.mb_amount;
        document.getElementById("evento").value = event_id;

        document.getElementById("pagar_tarde").submit();
    });
}


function listar(paginaAtual) {
    $(document).ready(function() {

        if (!lista_eventos) {
            return;
        }

        if (typeof lista_eventos === 'string') {
            try {
                lista_eventos = JSON.parse(lista_eventos);
                if (lista_eventos.length > 0) {
                    document.getElementById("inscricoes").style.display = "flex";
                }
            } catch (error) {
                return;
            }
        }

        const inscricoes = document.getElementById("inscricoes");
        const paginaInfo = paginacao(lista_eventos, paginaAtual, 4);
        const inscricoesPaginaAtual = paginaInfo.obj;

        while (inscricoes.firstChild) {
            inscricoes.removeChild(inscricoes.firstChild);
        }

        for (let i = 0; i < inscricoesPaginaAtual.length; i++) {
            const evento = document.createElement("div");
            evento.classList.add("evento");
            evento.setAttribute("id", "evento_" + i);

            let h = document.createElement("h3");
            let event_name = document.createElement("p");
            let participant_real_name = document.createElement("p");
            let participant_gender = document.createElement("p");
            let participant_type = document.createElement("p");
            let is_paid = document.createElement("p");


            h.innerHTML = "EVENTO";
            event_name.innerHTML = "Nome do Evento: " + inscricoesPaginaAtual[i].event_name;
            participant_real_name.innerHTML = "Nome do Participante: " + inscricoesPaginaAtual[i].participant_real_name;
            participant_gender.innerHTML = "Género: " + inscricoesPaginaAtual[i].participant_gender;
            participant_type.innerHTML = "Tipo de Participante: " + inscricoesPaginaAtual[i].participant_type;
            is_paid.innerHTML = "Está pago: " + inscricoesPaginaAtual[i].is_paid;

            evento.appendChild(h);
            evento.appendChild(event_name);
            evento.appendChild(participant_real_name);
            evento.appendChild(participant_gender);
            evento.appendChild(participant_type);
            evento.appendChild(is_paid);

            if(inscricoesPaginaAtual[i].is_paid === false) {
                let form = document.createElement("form");
                form.setAttribute("action", "/payLate");
                form.setAttribute("method", "POST");

                let pagar = document.createElement("input");
                pagar.setAttribute("class", "paymentButton");
                pagar.setAttribute("type", "submit");
                pagar.setAttribute("value", "Pagar");

                let input_event_id = document.createElement("input");
                input_event_id.setAttribute("type", "hidden");
                input_event_id.setAttribute("name", "event_id_pagar");
                input_event_id.value = inscricoesPaginaAtual[i].event_id;

                let csrf_token = document.createElement("input");
                csrf_token.setAttribute("type", "hidden");
                csrf_token.setAttribute("name", "_csrf");
                csrf_token.setAttribute("value", token);

                form.appendChild(input_event_id);
                form.appendChild(pagar);
                form.appendChild(csrf_token);
                evento.appendChild(form);
            }

            inscricoes.appendChild(evento);
        }

        const paginationContainer = document.getElementById("paginas");
        paginationContainer.innerHTML = "";

        if (paginaAtual > 1) {
            const prevButton = document.createElement("a");
            prevButton.innerHTML = "Anterior";
            prevButton.href = "#";
            prevButton.addEventListener("click", function () {
                listar(paginaAtual - 1);
            });
            paginationContainer.appendChild(prevButton);
        }
        if (paginaAtual < paginaInfo.pags) {
            const nextButton = document.createElement("a");
            nextButton.innerHTML = "Próxima";
            nextButton.href = "#";
            nextButton.addEventListener("click", function () {
                listar(paginaAtual + 1);
            });
            paginationContainer.appendChild(nextButton);
        }
        if (paginaInfo.pags > 0) {
            const p = document.createElement("p");
            p.innerHTML = paginaAtual + " de " + paginaInfo.pags;
            paginationContainer.appendChild(p);
        }
    });
}

function listar_pesquisa(paginaAtual) {
    $(document).ready(function () {

        // Isto verifica se está null e para de mandar as mensagens de erro na console
        if (!lista_eventos) {
            return;
        }

        if (typeof lista_eventos === 'string') {
            try {
                lista_eventos = JSON.parse(lista_eventos);
            } catch (error) {
                return;
            }
        }

        // Função de paginação para obter os eventos a serem exibidos em cada página
        const paginaInfo = paginacao(lista_eventos, paginaAtual, 4);
        const eventosPaginaAtual = paginaInfo.obj;

        const lista = document.getElementById("listaPesq");
        const title = document.getElementById("show");

        while (lista.firstChild) {
            lista.removeChild(lista.firstChild);
        }

        for (let i = 0; i < eventosPaginaAtual.length; i++) {
            const evento = document.createElement("div");
            evento.classList.add("evento");
            evento.setAttribute("id", "evento_" + i);
            let event_name = document.createElement("p");
            let event_description = document.createElement("p");
            let event_date = document.createElement("p");
            let join_value = document.createElement("p");
            const dataFormatada = new Date(eventosPaginaAtual[i].event_date).toISOString().split('T')[0];

            event_name.innerHTML = "Nome: " + eventosPaginaAtual[i].event_name;
            event_description.innerHTML = "Descrição: " + eventosPaginaAtual[i].event_description;
            event_date.innerHTML = "Data: " + dataFormatada;
            join_value.innerHTML = "Valor de inscrição: " + eventosPaginaAtual[i].join_value + "€";

            evento.appendChild(event_name);
            evento.appendChild(event_description);
            evento.appendChild(event_date);
            evento.appendChild(join_value);

            const verMaisButton = document.createElement("button");
            verMaisButton.innerHTML = "Ver mais";
            verMaisButton.id = "botao";

            let nome = eventosPaginaAtual[i].event_name;
            verMaisButton.addEventListener("click", function () {
                window.location.href = "/eventDetails?eventname=" + encodeURIComponent(nome);
            });

            evento.appendChild(verMaisButton);

            lista.appendChild(evento);
        }

        if (lista.innerHTML.trim() !== "") {
            title.style.display = "flex";
        }

        const paginationContainer = document.getElementById("paginas");
        paginationContainer.innerHTML = "";

        if (paginaAtual > 1) {
            const prevButton = document.createElement("a");
            prevButton.innerHTML = "Anterior";
            prevButton.href = "#";
            prevButton.addEventListener("click", function () {
                listar_pesquisa(paginaAtual - 1);
            });
            paginationContainer.appendChild(prevButton);
        }
        if (paginaAtual < paginaInfo.pags) {
            const nextButton = document.createElement("a");
            nextButton.innerHTML = "Próxima";
            nextButton.href = "#";
            nextButton.addEventListener("click", function () {
                listar_pesquisa(paginaAtual + 1);
            });
            paginationContainer.appendChild(nextButton);
        }
        if (paginaInfo.pags > 0) {
            const p = document.createElement("p");
            p.innerHTML = paginaAtual + " de " + paginaInfo.pags;
            paginationContainer.appendChild(p);
        }
    });
}

//Filtrar por nome ou por escalao na classificacao geral
function filterTable() {
    let inputName, inputType, filterName, filterType, table, tr, tdName, tdType, i, txtValueName,
        txtValueType;

    // Recebe input do username e do tipo
    inputName = document.getElementById("filterByName");
    inputType = document.getElementById("filterByType");

    // Filtra tanto o username como o tipo tudo para letras maíusculas, assim não é case sensitive
    filterName = inputName.value.toUpperCase();
    filterType = inputType.value.toUpperCase();

    // Recebe as linhas da tabela
    table = document.getElementById("body");
    tr = table.getElementsByTagName("tr");

    // Percorre as linhas da tabela e procura as células 1 e 3 para o username e o tipo respetivamente
    for (i = 0; i < tr.length; i++) {
        tdName = tr[i].getElementsByTagName("td")[1];
        tdType = tr[i].getElementsByTagName("td")[3];

        // Se existirem guarda nas variáveis para depois fazer a verificação
        if (tdName && tdType) {
            txtValueName = tdName.textContent || tdName.innerText;
            txtValueType = tdType.textContent || tdType.innerText;

            /* Se o nome inserido ocorrer pelo menos uma vez e
            não existir filtro de tipo ou o tipo seja igual ao tipo do input
            mostra essa linha, se não, esconde essa linha
             */
            if ((txtValueName.toUpperCase().indexOf(filterName) > -1) && (filterType === "" || txtValueType.toUpperCase() === filterType)) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}


//Algoritmo para paginacao
function paginacao(objeto, pagina, colunas) {
    const inicio = (pagina - 1) * colunas;
    const fim = inicio + colunas;

    const objeto_t = objeto.slice(inicio, fim);

    const paginas = Math.ceil(objeto.length / colunas);

    //Retorna o obj com os anúncios correspondentes a pagina e o número de paginas
    return {
        'obj': objeto_t,
        'pags': paginas,
    }
}