
-- 1. Remoção das tabelas existentes para garantir que o seed limpe o banco antes de recriar
DROP TABLE IF EXISTS public.questions CASCADE;
DROP TABLE IF EXISTS public.lessons CASCADE;
DROP TABLE IF EXISTS public.courses CASCADE;

-- 2. Criação da tabela de Cursos
-- Corresponde a: object Courses : IntIdTable("courses") em Tables.kt
CREATE TABLE IF NOT EXISTS public.courses (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,                    -- nullable (como no Tables.kt)
    icon TEXT,                           -- nullable
    color TEXT,                          -- nullable
    total_lessons INTEGER DEFAULT 0      -- nullable com default 0
);

-- 3. Criação da tabela de Lições
-- Corresponde a: object Lessons : IntIdTable("lessons") em Tables.kt
CREATE TABLE IF NOT EXISTS public.lessons (
    id SERIAL PRIMARY KEY,
    course_id INTEGER NOT NULL REFERENCES public.courses(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT,                    -- nullable
    "order" INTEGER,                     -- nullable (aspas pq order é palavra reservada do SQL)
    difficulty_level TEXT                -- nullable (adicionado na V2)
);

-- 4. Criação da tabela de Questões
-- Corresponde a: object Questions : IntIdTable("questions") em Tables.kt
CREATE TABLE IF NOT EXISTS public.questions (
    id SERIAL PRIMARY KEY,
    lesson_id INTEGER NOT NULL REFERENCES public.lessons(id) ON DELETE CASCADE,
    question TEXT NOT NULL,
    code TEXT,                           -- nullable
    options TEXT NOT NULL DEFAULT '[]',   -- TEXT (não JSONB), como no Tables.kt
    correct_answer INTEGER,              -- nullable
    "order" INTEGER                      -- nullable
);

-- Habilitar Row Level Security (RLS) - por enquanto liberado para testes (anon)
ALTER TABLE public.courses ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.lessons ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.questions ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow anon read/write access courses" ON public.courses FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow anon read/write access lessons" ON public.lessons FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow anon read/write access questions" ON public.questions FOR ALL USING (true) WITH CHECK (true);

-- 5. Inserindo dados iniciais (Seed) para Cursos
INSERT INTO public.courses (id, title, description, icon, color, total_lessons) VALUES
(1, 'Java Icaro', 'Aprenda os fundamentos da linguagem de programação Java', 'code', '#E76F00', 4),
(2, 'Kotlin', 'Domine a programação em Kotlin desde o início', 'code', '#7F52FF', 4),
(3, 'Python', 'Explore os fundamentos da programação em Python', 'code', '#3776AB', 4),
(4, 'TypeScript', 'Construa aplicações com tipagem segura usando TypeScript', 'code', '#3178C6', 4)
ON CONFLICT (id) DO UPDATE SET 
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    icon = EXCLUDED.icon,
    color = EXCLUDED.color,
    total_lessons = EXCLUDED.total_lessons;

-- Ajustando a sequência de Cursos para iniciar pós-seed
SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses));

-- 6. Inserindo dados iniciais (Seed) para Lições
INSERT INTO public.lessons (id, course_id, title, description, "order") VALUES
-- Java (course 1)
(1, 1, 'Variáveis', 'Tipos de dados, declarações e inicialização em Java', 1),
(2, 1, 'Laços de Repetição', 'Domine for, while e do-while em Java', 2),
(3, 1, 'Funções', 'Métodos, parâmetros e tipos de retorno', 3),
(4, 1, 'Classes', 'Programação orientada a objetos com classes Java', 4),
-- Kotlin (course 2)
(5, 2, 'Variáveis', 'Entenda val, var e inferência de tipo em Kotlin', 1),
(6, 2, 'Laços de Repetição', 'Explore for, while e loops baseados em range', 2),
(7, 2, 'Funções', 'Funções, lambdas e extensões em Kotlin', 3),
(8, 2, 'Classes', 'Data classes, sealed classes e herança', 4),
-- Python (course 3)
(9, 3, 'Variáveis', 'Tipagem dinâmica, atribuições e tipos de dados', 1),
(10, 3, 'Laços de Repetição', 'for-in, while e list comprehensions', 2),
(11, 3, 'Funções', 'def, parâmetros padrão, *args e **kwargs', 3),
(12, 3, 'Classes', 'POO em Python, __init__, herança e métodos especiais', 4),
-- TypeScript (course 4)
(13, 4, 'Variáveis', 'let, const, anotações de tipo e inferência', 1),
(14, 4, 'Laços de Repetição', 'for, for-of, while e métodos de array', 2),
(15, 4, 'Funções', 'Funções tipadas, arrow functions e generics', 3),
(16, 4, 'Classes', 'Classes, interfaces e modificadores de acesso', 4)
ON CONFLICT (id) DO UPDATE SET 
    course_id = EXCLUDED.course_id,
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    "order" = EXCLUDED."order",
    difficulty_level = EXCLUDED.difficulty_level;

-- Ajustando a sequência de Lições para iniciar pós-seed
SELECT setval('lessons_id_seq', (SELECT MAX(id) FROM lessons));

-- 7. Inserindo dados iniciais (Seed) para Questões
INSERT INTO public.questions (id, lesson_id, question, code, options, correct_answer, "order") VALUES
-- === Java — Lição 1: Variáveis ===
(1, 1, 'Qual é o tipo primitivo usado para armazenar números inteiros em Java?', NULL, '["float","int","String","boolean"]', 1, 1),
(2, 1, 'Qual palavra reservada é usada para declarar uma constante em Java?', NULL, '["const","static","final","let"]', 2, 2),
(3, 1, 'Qual é o valor padrão de uma variável int não inicializada como atributo de classe?', NULL, '["null","0","undefined","-1"]', 1, 3),
(4, 1, 'Qual será o resultado deste código?', E'int x = 10;\ndouble y = x;\nSystem.out.println(y);', '["10","10.0","Erro de compilação","null"]', 1, 4),
(5, 1, 'O que este código imprime?', E'String nome = "Java";\nint versao = 21;\nSystem.out.println(nome + " " + versao);', '["Java21","Java 21","Erro de compilação","null 21"]', 1, 5),

-- === Java — Lição 2: Laços ===
(6, 2, 'Qual laço garante que o bloco será executado pelo menos uma vez?', NULL, '["for","while","do-while","foreach"]', 2, 1),
(7, 2, 'Qual palavra-chave é usada para pular uma iteração do loop?', NULL, '["break","skip","continue","pass"]', 2, 2),
(8, 2, 'Quantas vezes o loop executa?', E'for (int i = 0; i < 5; i++) {\n    System.out.println(i);\n}', '["4","5","6","Infinito"]', 1, 3),
(9, 2, 'Qual é a saída deste código?', E'int i = 3;\nwhile (i > 0) {\n    System.out.print(i + " ");\n    i--;\n}', '["3 2 1","3 2 1 0","2 1 0","Erro"]', 0, 4),
(10, 2, 'O que acontece ao executar este código?', E'for (int i = 0; i < 3; i++) {\n    if (i == 1) continue;\n    System.out.print(i + " ");\n}', '["0 2","0 1 2","1 2","0"]', 0, 5),

-- === Java — Lição 3: Funções ===
(11, 3, 'Qual palavra-chave indica que um método não retorna nenhum valor?', NULL, '["null","void","empty","none"]', 1, 1),
(12, 3, 'O que é sobrecarga de métodos (overloading)?', NULL, '["Usar o mesmo nome com parâmetros diferentes","Reutilizar um método de outra classe","Chamar um método recursivamente","Criar métodos estáticos"]', 0, 2),
(13, 3, 'Qual é o retorno deste método?', E'public static int soma(int a, int b) {\n    return a + b;\n}\n// Chamada: soma(3, 7)', '["37","10","Erro","null"]', 1, 3),
(14, 3, 'O que este método retorna?', E'public static boolean ehPar(int n) {\n    return n % 2 == 0;\n}\n// Chamada: ehPar(5)', '["true","false","0","Erro"]', 1, 4),
(15, 3, 'Quantas vezes a mensagem é impressa?', E'public static void repetir(String msg, int vezes) {\n    for (int i = 0; i < vezes; i++) {\n        System.out.println(msg);\n    }\n}\n// Chamada: repetir("Oi", 3)', '["1","2","3","0"]', 2, 5),

-- === Java — Lição 4: Classes ===
(16, 4, 'Qual é o método especial chamado ao criar um objeto em Java?', NULL, '["init()","create()","construtor","new()"]', 2, 1),
(17, 4, 'Qual modificador de acesso permite que apenas a própria classe acesse o atributo?', NULL, '["public","protected","private","default"]', 2, 2),
(18, 4, 'O que significa herança em Java?', NULL, '["Uma classe herda atributos e métodos de outra","Copiar código entre arquivos","Criar variáveis globais","Importar bibliotecas"]', 0, 3),
(19, 4, 'O que este código imprime?', E'class Carro {\n    String modelo;\n    Carro(String modelo) {\n        this.modelo = modelo;\n    }\n}\nCarro c = new Carro("Civic");\nSystem.out.println(c.modelo);', '["Carro","Civic","null","Erro"]', 1, 4),
(20, 4, 'Qual é a saída?', E'class Animal {\n    String falar() { return "..."; }\n}\nclass Gato extends Animal {\n    String falar() { return "Miau"; }\n}\nAnimal a = new Gato();\nSystem.out.println(a.falar());', '["...","Miau","Erro","null"]', 1, 5),

-- === Kotlin — Lição 5: Variáveis ===
(21, 5, 'Qual palavra-chave define uma variável imutável (constante) em Kotlin?', NULL, '["var","final","val","const"]', 2, 1),
(22, 5, 'Como o Kotlin lida com valores nulos por padrão?', NULL, '["Permite null em qualquer tipo","Tipos são não-nulos por padrão","Usa Optional igual Java","Ignora null"]', 1, 2),
(23, 5, 'O que é inferência de tipo em Kotlin?', NULL, '["O compilador deduz o tipo baseado no valor","Tipos são dinâmicos","Toda variável é Any","Não existe tipagem forte"]', 0, 3),
(24, 5, 'Qual é o tipo da variável `x`?', E'val x = 10.5', '["Int","Double","Float","Number"]', 1, 4),
(25, 5, 'O que acontece neste código?', E'var nome: String = "Kotlin"\nnome = null', '["Erro de compilação","Compila normal","Imprime null","Crash"]', 0, 5),

-- === Kotlin — Lição 6: Laços ===
(26, 6, 'Qual a sintaxe correta para iterar um range inclusivo?', NULL, '["for (i in 1..5)","for (i = 1; i <= 5; i++)","foreach (i : 1..5)","loop 1 to 5"]', 0, 1),
(27, 6, 'Como fazer um loop decrescente?', NULL, '["downTo","minus","step -1","reverse"]', 0, 2),
(28, 6, 'Quantas vezes imprime?', E'for (i in 1..3) print(i)', '["2","3","4","1"]', 1, 3),
(29, 6, 'Qual a saída?', E'for (i in 1 until 4) print(i)', '["1234","123","12","Erro"]', 1, 4),
(30, 6, 'O que imprime?', E'var x = 3\nwhile(x > 0) {\n  print(x)\n  x--\n}', '["321","3210","210","Infinito"]', 0, 5),

-- === Kotlin — Lição 7: Funções ===
(31, 7, 'Qual palavra reservada inicia a declaração de função?', NULL, '["func","def","fun","function"]', 2, 1),
(32, 7, 'O que é uma Single-Expression Function?', NULL, '["Uma função sem corpo entre chaves usando =","Uma função lambda","Uma função anônima","Uma função privada"]', 0, 2),
(33, 7, 'O que retorna?', E'fun soma(a: Int, b: Int) = a + b\nprintln(soma(2, 3))', '["23","5","Erro","Unit"]', 1, 3),
(34, 7, 'Qual o tipo de retorno padrão se não especificado?', NULL, '["void","null","Unit","Any"]', 2, 4),
(35, 7, 'O que este código faz?', E'fun String.ola() = "Olá $this"\nprintln("Mundo".ola())', '["Olá Mundo","Erro","Mundo Ola","Null"]', 0, 5),

-- === Kotlin — Lição 8: Classes ===
(36, 8, 'Qual classe é usada automaticamente para conter dados?', NULL, '["struct","record","data class","pojo"]', 2, 1),
(37, 8, 'Classes em Kotlin são por padrão...', NULL, '["final (fechadas)","open (abertas)","abstract","static"]', 0, 2),
(38, 8, 'Como declarar um construtor primário?', NULL, '["class User(val nome: String)","constructor User()","def init()","class User { init() }"]', 0, 3),
(39, 8, 'O que imprime?', E'data class User(val nome: String)\nval u1 = User("Ana")\nval u2 = User("Ana")\nprintln(u1 == u2)', '["true","false","Erro","Depende da memória"]', 0, 4),
(40, 8, 'Qual a saída?', E'open class A\nclass B : A()\nprintln(B() is A)', '["true","false","Erro","null"]', 0, 5),

-- === Python — Lição 9: Variáveis ===
(41, 9, 'Python necessita de declaração explícita de tipos?', NULL, '["Sim","Não, é dinâmico","Apenas para strings","Depende da versão"]', 1, 1),
(42, 9, 'Como verificar o tipo de uma variável?', NULL, '["typeof(x)","type(x)","instanceof(x)","check(x)"]', 1, 2),
(43, 9, 'Qual o valor de x?', E'x = 10\nx = "texto"\nprint(x)', '["10","texto","Erro de tipo","null"]', 1, 3),
(44, 9, 'Como se cria uma lista vazia?', NULL, '["list() ou []","new List()","Array()","{}"]', 0, 4),
(45, 9, 'O que imprime?', E'a = [1, 2, 3]\nb = a\nb.append(4)\nprint(len(a))', '["3","4","Erro","0"]', 1, 5),

-- === Python — Lição 10: Laços ===
(46, 10, 'Qual função gera uma sequência de números?', NULL, '["seq()","range()","xrange()","list()"]', 1, 1),
(47, 10, 'Como interromper um loop imediatamente?', NULL, '["stop","exit","break","halt"]', 2, 2),
(48, 10, 'Quantas vezes imprime?', E'for i in range(5):\n    print(i)', '["4","5","6","1"]', 1, 3),
(49, 10, 'Qual a saída?', E'x = [i*2 for i in range(3)]\nprint(x)', '["[0, 1, 2]","[0, 2, 4]","[2, 4, 6]","[1, 2, 3]"]', 1, 4),
(50, 10, 'O que imprime?', E'i = 0\nwhile i < 3:\n    print(i, end="")\n    i += 1', '["0 1 2","012","123","01"]', 1, 5),

-- === Python — Lição 11: Funções ===
(51, 11, 'Qual palavra define uma função em Python?', NULL, '["func","fun","function","def"]', 3, 1),
(52, 11, 'Como definir valor padrão para parâmetro?', NULL, '["def f(p: 0)","def f(p=0)","def f(p == 0)","def f(p -> 0)"]', 1, 2),
(53, 11, 'O que retorna?', E'def soma(a, b):\n    return a + b\nprint(soma("Oj", "a"))', '["Oja","Erro","NaN","null"]', 0, 3),
(54, 11, 'Qual a saída?', E'def f(x=[]):\n    x.append(1)\n    return x\nprint(f()); print(f())', '["[1] [1]","[1] [1, 1]","[1] []","Erro"]', 1, 4),
(55, 11, 'O que args captura?', E'def f(*args):\n    print(type(args))', '["list","tuple","dict","set"]', 1, 5),

-- === Python — Lição 12: Classes ===
(56, 12, 'Qual é o nome do método construtor?', NULL, '["constructor","__init__","init","new"]', 1, 1),
(57, 12, 'O que representa o primeiro parâmetro de métodos (self)?', NULL, '["A instância atual","A classe","O módulo","Nada"]', 0, 2),
(58, 12, 'Como indicar herança?', NULL, '["class A(B):","class A extends B:","class A : B","class A inherits B"]', 0, 3),
(59, 12, 'Qual a saída?', E'class Cao:\n    kind = "canino"\nc = Cao()\nc.kind = "lobo"\nprint(Cao.kind)', '["lobo","canino","Erro","null"]', 1, 4),
(60, 12, 'O que imprime?', E'class A:\n    def __str__(self): return "A"\nprint(A())', '["<Object A>","A","Endereço de memória","Erro"]', 1, 5),

-- === TypeScript — Lição 13: Variáveis ===
(61, 13, 'Qual é o superset que TypeScript estende?', NULL, '["Java","C#","JavaScript","Python"]', 2, 1),
(62, 13, 'Como tipar explicitamente uma variável numérica?', NULL, '["let n: number","let n: int","var n = (int)","const n :: Number"]', 0, 2),
(63, 13, 'Qual tipo representa "qualquer coisa"?', NULL, '["Object","void","any","unknown"]', 2, 3),
(64, 13, 'O que acontece?', E'let x: string = "TS";\nx = 10;', '["Erro de compilação TS","Funciona pois é JS","Apenas warning","Crash"]', 0, 4),
(65, 13, 'Qual a saída?', E'const lista: number[] = [1, 2];\nlista.push("3");', '["[1, 2, ''3'']","Erro de tipo","NaN","null"]', 1, 5),

-- === TypeScript — Lição 14: Laços ===
(66, 14, 'Qual loop itera sobre valores de um array?', NULL, '["for..in","for..of","foreach","loop"]', 1, 1),
(67, 14, 'Qual método de array cria um novo array transformado?', NULL, '["forEach","map","filter","reduce"]', 1, 2),
(68, 14, 'O que imprime?', E'const a = [10, 20];\nfor (let i of a) console.log(i);', '["0 1","10 20","undefined","Erro"]', 1, 3),
(69, 14, 'Qual a saída?', E'let i = 0;\ndo { i++; } while (i < 0);\nconsole.log(i);', '["0","1","Erro","-1"]', 1, 4),
(70, 14, 'O que filter faz?', E'const nums = [1, 2, 3, 4];\nconsole.log(nums.filter(n => n % 2 === 0));', '["[1, 3]","[2, 4]","[true, false, true, false]","[2]"]', 1, 5),

-- === TypeScript — Lição 15: Funções ===
(71, 15, 'Como definir retorno opcional?', NULL, '["func f(): void?","func f()?","Retorno é sempre obrigatório","void | undefined"]', 3, 1),
(72, 15, 'Sintaxe de Arrow Function:', NULL, '["=>","->","function","def"]', 0, 2),
(73, 15, 'O que este código retorna?', E'const soma = (a: number, b: number) => a + b;\nconsole.log(soma(2, "2"));', '["4","22","Erro de tipo","NaN"]', 2, 3),
(74, 15, 'Qual o tipo de retorno?', E'function nada(): void { return 1; }', '["number","void","Erro TS","undefined"]', 2, 4),
(75, 15, 'Generics servem para...', NULL, '["Criar componentes reutilizáveis e tipados","Aumentar performance","Ofuscar código","Validar em runtime"]', 0, 5),

-- === TypeScript — Lição 16: Classes ===
(76, 16, 'Qual modificador deixa o atributo acessível apenas na classe?', NULL, '["public","static","private","readonly"]', 2, 1),
(77, 16, 'O que é uma interface em TS?', NULL, '["Um contrato de estrutura de objeto","Uma classe compilada","Uma função global","Uma variável"]', 0, 2),
(78, 16, 'Como implementar uma interface?', NULL, '["class A implements I","class A extends I","class A inherits I","class A : I"]', 0, 3),
(79, 16, 'O que imprime?', E'class A { static x = 10 }\nconsole.log(A.x)', '["undefined","10","Erro","null"]', 1, 4),
(80, 16, 'Qual a saída?', E'interface User { name: string }\nconst u: User = { name: 10 };', '["{ name: 10 }","Erro de tipo","null","undefined"]', 1, 5)
ON CONFLICT (id) DO UPDATE SET 
    lesson_id = EXCLUDED.lesson_id,
    question = EXCLUDED.question,
    code = EXCLUDED.code,
    options = EXCLUDED.options,
    correct_answer = EXCLUDED.correct_answer,
    "order" = EXCLUDED."order";

-- Ajustando a sequência de Questões para iniciar pós-seed
SELECT setval('questions_id_seq', (SELECT MAX(id) FROM questions));
