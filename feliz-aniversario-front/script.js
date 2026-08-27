const API_BASE = "http://localhost:8080/api/aluno";
const API_MENSAGEM_LISTAGEM = "http://localhost:8080/api/mensagem/listagem";

const modalAluno = document.getElementById("modal");
const modalTitle = document.getElementById("modal-title");
const statusAlunos = document.getElementById("status");
const statusMensagens = document.getElementById("status-mensagens");
const tbodyAlunos = document.getElementById("tbody-alunos");
const tbodyMensagens = document.getElementById("tbody-mensagens");
const form = document.getElementById("aluno-form");
const secaoAlunos = document.getElementById("secao-alunos");
const secaoMensagens = document.getElementById("secao-mensagens");

const modalMensagem = document.getElementById("modal-mensagem");
const mensagemTexto = document.getElementById("mensagem-texto");

const fieldId = document.getElementById("aluno-id");
const fieldMatricula = document.getElementById("matricula");
const fieldNome = document.getElementById("nome");
const fieldEmail = document.getElementById("email");
const fieldTurma = document.getElementById("turma");
const fieldDataNascimento = document.getElementById("dataNascimento");

let alunosCache = [];
const mensagensMap = new Map();

document.getElementById("menu-alunos").addEventListener("click", async () => {
    exibirSecao("alunos");
    closeModalAluno();
    closeModalMensagem();
    await carregarAlunos();
});

document.getElementById("menu-mensagens").addEventListener("click", async () => {
    exibirSecao("mensagens");
    closeModalAluno();
    await carregarMensagens();
});

document.getElementById("btn-novo-aluno").addEventListener("click", () => openCreateModal());
document.getElementById("btn-recarregar").addEventListener("click", async () => {
    await carregarAlunos();
    if (!secaoMensagens.classList.contains("hidden")) {
        await carregarMensagens();
    }
});

document.getElementById("modal-close").addEventListener("click", closeModalAluno);
document.getElementById("btn-cancelar").addEventListener("click", closeModalAluno);
document.getElementById("modal-mensagem-close").addEventListener("click", closeModalMensagem);
document.getElementById("btn-mensagem-fechar").addEventListener("click", closeModalMensagem);

modalAluno.addEventListener("click", (event) => {
    if (event.target === modalAluno) {
        closeModalAluno();
    }
});

modalMensagem.addEventListener("click", (event) => {
    if (event.target === modalMensagem) {
        closeModalMensagem();
    }
});

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const payload = {
        matricula: Number(fieldMatricula.value),
        nome: fieldNome.value.trim(),
        email: fieldEmail.value.trim(),
        turma: fieldTurma.value.trim(),
        dataNascimento: fieldDataNascimento.value
    };

    const id = fieldId.value;
    const method = id ? "PUT" : "POST";
    const url = id ? `${API_BASE}/${id}` : API_BASE;

    try {
        await request(url, method, payload);
        closeModalAluno();
        await carregarAlunos();
        if (!secaoMensagens.classList.contains("hidden")) {
            await carregarMensagens();
        }
        statusAlunos.textContent = id ? "Aluno atualizado com sucesso." : "Aluno cadastrado com sucesso.";
    } catch (error) {
        statusAlunos.textContent = error.message;
    }
});

tbodyAlunos.addEventListener("click", async (event) => {
    const target = event.target;
    if (!(target instanceof HTMLButtonElement)) {
        return;
    }

    const action = target.dataset.action;
    const id = Number(target.dataset.id);
    if (!id || !action) {
        return;
    }

    if (action === "editar") {
        const aluno = alunosCache.find((item) => item.id === id);
        if (!aluno) {
            statusAlunos.textContent = "Aluno não encontrado para edição.";
            return;
        }
        openEditModal(aluno);
        return;
    }

    if (action === "excluir") {
        const confirmou = window.confirm("Deseja excluir este aluno?");
        if (!confirmou) {
            return;
        }

        try {
            await request(`${API_BASE}/${id}`, "DELETE");
            await carregarAlunos();
            if (!secaoMensagens.classList.contains("hidden")) {
                await carregarMensagens();
            }
            statusAlunos.textContent = "Aluno excluído com sucesso.";
        } catch (error) {
            statusAlunos.textContent = error.message;
        }
    }
});

tbodyMensagens.addEventListener("click", (event) => {
    const target = event.target;
    if (!(target instanceof HTMLButtonElement)) {
        return;
    }

    const key = target.dataset.msgId;
    if (!key) {
        return;
    }

    const texto = mensagensMap.get(key) || "Mensagem sem texto.";
    openModalMensagem(texto);
});

function exibirSecao(secao) {
    const alunosAtivo = secao === "alunos";
    document.getElementById("menu-alunos").classList.toggle("active", alunosAtivo);
    document.getElementById("menu-mensagens").classList.toggle("active", !alunosAtivo);
    secaoAlunos.classList.toggle("hidden", !alunosAtivo);
    secaoMensagens.classList.toggle("hidden", alunosAtivo);
}

function openCreateModal() {
    exibirSecao("alunos");
    form.reset();
    fieldId.value = "";
    modalTitle.textContent = "Novo aluno";
    modalAluno.classList.remove("hidden");
    modalAluno.setAttribute("aria-hidden", "false");
}

function openEditModal(aluno) {
    exibirSecao("alunos");
    fieldId.value = aluno.id;
    fieldMatricula.value = aluno.matricula ?? "";
    fieldNome.value = aluno.nome ?? "";
    fieldEmail.value = aluno.email ?? "";
    fieldTurma.value = aluno.turma ?? "";
    fieldDataNascimento.value = aluno.dataNascimento ?? "";
    modalTitle.textContent = "Editar aluno";
    modalAluno.classList.remove("hidden");
    modalAluno.setAttribute("aria-hidden", "false");
}

function closeModalAluno() {
    modalAluno.classList.add("hidden");
    modalAluno.setAttribute("aria-hidden", "true");
}

function openModalMensagem(texto) {
    mensagemTexto.textContent = texto;
    modalMensagem.classList.remove("hidden");
    modalMensagem.setAttribute("aria-hidden", "false");
}

function closeModalMensagem() {
    modalMensagem.classList.add("hidden");
    modalMensagem.setAttribute("aria-hidden", "true");
}

function parseISODate(isoDate) {
    if (!isoDate || typeof isoDate !== "string") {
        return null;
    }
    const [year, month, day] = isoDate.split("-").map(Number);
    if (!year || !month || !day) {
        return null;
    }
    return new Date(year, month - 1, day);
}

function getProximoAniversario(dataNascimento) {
    const hoje = new Date();
    const nascimento = parseISODate(dataNascimento);
    if (!nascimento) {
        return null;
    }
    const proximo = new Date(hoje.getFullYear(), nascimento.getMonth(), nascimento.getDate());

    proximo.setHours(0, 0, 0, 0);
    hoje.setHours(0, 0, 0, 0);

    if (proximo < hoje) {
        proximo.setFullYear(proximo.getFullYear() + 1);
    }

    return proximo;
}

function diferencaEmDias(dataFutura) {
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    const diffMs = dataFutura - hoje;
    return Math.round(diffMs / 86400000);
}

function formatarData(data) {
    return data ? data.toLocaleDateString("pt-BR") : "-";
}

function formatarDataISO(isoDate) {
    return formatarData(parseISODate(isoDate));
}

function renderTabelaAlunos(alunos) {
    tbodyAlunos.innerHTML = "";

    if (alunos.length === 0) {
        tbodyAlunos.innerHTML = "<tr><td colspan='8'>Nenhum aluno cadastrado.</td></tr>";
        return 0;
    }

    const proximos20 = alunos
        .filter((aluno) => Boolean(aluno.dataNascimento))
        .map((aluno) => {
            const proximoAniversario = getProximoAniversario(aluno.dataNascimento);
            return proximoAniversario
                ? { ...aluno, proximoAniversario, diasRestantes: diferencaEmDias(proximoAniversario) }
                : null;
        })
        .filter(Boolean)
        .sort((a, b) => a.diasRestantes - b.diasRestantes || (a.nome || "").localeCompare(b.nome || ""))
        .slice(0, 20);

    if (proximos20.length === 0) {
        tbodyAlunos.innerHTML = "<tr><td colspan='8'>Nenhum aluno com data de nascimento válida.</td></tr>";
        return 0;
    }

    proximos20.forEach((aluno) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${aluno.matricula ?? ""}</td>
            <td>${aluno.nome ?? ""}</td>
            <td>${aluno.email ?? ""}</td>
            <td>${aluno.turma ?? ""}</td>
            <td>${formatarDataISO(aluno.dataNascimento)}</td>
            <td>${formatarData(aluno.proximoAniversario)}</td>
            <td>${aluno.diasRestantes}</td>
            <td>
                <div class="action-group">
                    <button class="secondary-btn row-icon-btn" data-action="editar" data-id="${aluno.id}" title="Editar" aria-label="Editar">✏️</button>
                    <button class="danger-btn row-icon-btn" data-action="excluir" data-id="${aluno.id}" title="Excluir" aria-label="Excluir">🗑️</button>
                </div>
            </td>
        `;
        tbodyAlunos.appendChild(tr);
    });

    return proximos20.length;
}

function renderTabelaMensagens(listaMensagens) {
    mensagensMap.clear();
    tbodyMensagens.innerHTML = "";

    const mensagens = (Array.isArray(listaMensagens) ? listaMensagens : []).map((mensagem, index) => {
        const chave = mensagem.id != null ? String(mensagem.id) : `msg-${index}`;
        mensagensMap.set(chave, mensagem.texto || "Mensagem sem texto.");
        return {
            chave,
            nomeAluno: mensagem.nomeAluno ?? "",
            dataAniversario: mensagem.dataAniversario ?? "",
            dataEnvio: mensagem.dataEnvio ?? ""
        };
    });

    mensagens.sort((a, b) => {
        const dataA = a.dataEnvio || "";
        const dataB = b.dataEnvio || "";
        if (dataA === dataB) {
            return a.nomeAluno.localeCompare(b.nomeAluno);
        }
        return dataB.localeCompare(dataA);
    });

    if (mensagens.length === 0) {
        tbodyMensagens.innerHTML = "<tr><td colspan='4'>Nenhuma mensagem encontrada.</td></tr>";
        return 0;
    }

    mensagens.forEach((item) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${item.nomeAluno}</td>
            <td>${formatarDataISO(item.dataAniversario)}</td>
            <td>${formatarDataISO(item.dataEnvio)}</td>
            <td>
                <button class="message-btn row-icon-btn" data-msg-id="${item.chave}" title="Ver mensagem" aria-label="Ver mensagem">💬</button>
            </td>
        `;
        tbodyMensagens.appendChild(tr);
    });

    return mensagens.length;
}

async function request(url, method, body) {
    const response = await fetch(url, {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: body ? JSON.stringify(body) : undefined
    });

    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Erro ${response.status}: ${text || "não foi possível concluir a operação."}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function carregarAlunos() {
    statusAlunos.textContent = "Carregando alunos...";
    try {
        alunosCache = await request(API_BASE, "GET");
        const totalExibido = renderTabelaAlunos(alunosCache);
        statusAlunos.textContent = `${totalExibido} aluno(s) exibido(s), ordenados por proximidade.`;
    } catch (error) {
        statusAlunos.textContent = error.message;
        tbodyAlunos.innerHTML = "<tr><td colspan='8'>Não foi possível carregar os alunos.</td></tr>";
    }
}

async function carregarMensagens() {
    statusMensagens.textContent = "Carregando mensagens...";
    try {
        const mensagens = await request(API_MENSAGEM_LISTAGEM, "GET");
        const totalMensagens = renderTabelaMensagens(mensagens);
        statusMensagens.textContent = `${totalMensagens} mensagem(ns) encontrada(s).`;
    } catch (error) {
        statusMensagens.textContent = error.message;
        tbodyMensagens.innerHTML = "<tr><td colspan='4'>Não foi possível carregar as mensagens.</td></tr>";
    }
}

carregarAlunos();
