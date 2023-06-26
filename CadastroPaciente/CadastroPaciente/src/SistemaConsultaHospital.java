import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Paciente {
    private String nome;
    private String cpf;
    private String nascimento;
    private String telefone;
    private String sexo;
    private String email;

    public Paciente(String nome, String cpf, String nascimento, String telefone, String sexo, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.nascimento = nascimento;
        this.telefone = telefone;
        this.sexo = sexo;
        this.email = email;
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

public class SistemaConsultaHospital {
    private static List<Paciente> listaPacientes = new ArrayList<>();
    private static Connection connection;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistemaconsulta", "root", "");

            do {
                System.out.println("\nSistema de Consulta Hospitalar Público");
                System.out.println("1. Cadastrar paciente");
                System.out.println("2. Consultar paciente");
                System.out.println("3. Atualizar paciente");
                System.out.println("4. Remover paciente");
                System.out.println("5. Mostrar todos os pacientes");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        cadastrarPaciente(scanner);
                        break;
                    case 2:
                        consultarPaciente(scanner);
                        break;
                    case 3:
                        atualizarPaciente(scanner);
                        break;
                    case 4:
                        removerPaciente(scanner);
                        break;
                    case 5:
                        mostrarPacientes();
                        break;
                    case 0:
                        System.out.println("Saindo do programa...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (opcao != 0);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
                }
            }
        }
    }

    private static void cadastrarPaciente(Scanner scanner) {
        System.out.println("\n--- Cadastro de Paciente ---");
        System.out.print("Nome: ");
        String nome = scanner.next();

        System.out.print("CPF: ");
        String cpf = scanner.next();

        System.out.print("Nascimento: ");
        String nascimento = scanner.next();

        System.out.print("Telefone: ");
        String telefone = scanner.next();

        System.out.print("Sexo: ");
        String sexo = scanner.next();

        System.out.print("Email: ");
        String email = scanner.next();

        Paciente paciente = new Paciente(nome, cpf, nascimento, telefone, sexo, email);
        listaPacientes.add(paciente);

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO pacientes (nome, cpf, nascimento, telefone, sexo, email) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, nome);
            statement.setString(2, cpf);
            statement.setString(3, nascimento);
            statement.setString(4, telefone);
            statement.setString(5, sexo);
            statement.setString(6, email);
            statement.executeUpdate();
            System.out.println("\nPaciente cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar paciente no banco de dados: " + e.getMessage());
        }
    }

    private static void consultarPaciente(Scanner scanner) {
        System.out.println("\n--- Consulta de Paciente ---");
        System.out.print("CPF do paciente: ");
        String cpf = scanner.next();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM pacientes WHERE cpf = ?");
            statement.setString(1, cpf);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String nascimento = resultSet.getString("nascimento");
                String telefone = resultSet.getString("telefone");
                String sexo = resultSet.getString("sexo");
                String email = resultSet.getString("email");

                System.out.println("\nNome: " + nome);
                System.out.println("CPF: " + cpf);
                System.out.println("Nascimento: " + nascimento);
                System.out.println("Telefone: " + telefone);
                System.out.println("Sexo: " + sexo);
                System.out.println("Email: " + email);
            } else {
                System.out.println("\nPaciente não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar paciente no banco de dados: " + e.getMessage());
        }
    }

    private static void atualizarPaciente(Scanner scanner) {
        System.out.println("\n--- Atualização de Paciente ---");
        System.out.print("CPF do paciente: ");
        String cpf = scanner.next();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM pacientes WHERE cpf = ?");
            statement.setString(1, cpf);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String nascimento = resultSet.getString("nascimento");
                String telefone = resultSet.getString("telefone");
                String sexo = resultSet.getString("sexo");
                String email = resultSet.getString("email");

                System.out.print("Novo nome (" + nome + "): ");
                String novoNome = scanner.next();
                if (!novoNome.isEmpty()) {
                    nome = novoNome;
                }

                System.out.print("Novo nascimento (" + nascimento + "): ");
                String novoNascimento = scanner.next();
                if (!novoNascimento.isEmpty()) {
                    nascimento = novoNascimento;
                }

                System.out.print("Novo telefone (" + telefone + "): ");
                String novoTelefone = scanner.next();
                if (!novoTelefone.isEmpty()) {
                    telefone = novoTelefone;
                }

                System.out.print("Novo sexo (" + sexo + "): ");
                String novoSexo = scanner.next();
                if (!novoSexo.isEmpty()) {
                    sexo = novoSexo;
                }

                System.out.print("Novo email (" + email + "): ");
                String novoEmail = scanner.next();
                if (!novoEmail.isEmpty()) {
                    email = novoEmail;
                }

                statement = connection.prepareStatement("UPDATE pacientes SET nome = ?, nascimento = ?, telefone = ?, sexo = ?, email = ? WHERE cpf = ?");
                statement.setString(1, nome);
                statement.setString(2, nascimento);
                statement.setString(3, telefone);
                statement.setString(4, sexo);
                statement.setString(5, email);
                statement.setString(6, cpf);
                statement.executeUpdate();

                System.out.println("\nPaciente atualizado com sucesso!");
            } else {
                System.out.println("\nPaciente não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar paciente no banco de dados: " + e.getMessage());
        }
    }

    private static void removerPaciente(Scanner scanner) {
        System.out.println("\n--- Remoção de Paciente ---");
        System.out.print("CPF do paciente: ");
        String cpf = scanner.next();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM pacientes WHERE cpf = ?");
            statement.setString(1, cpf);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nPaciente removido com sucesso!");
            } else {
                System.out.println("\nPaciente não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover paciente do banco de dados: " + e.getMessage());
        }
    }

    private static void mostrarPacientes() {
        System.out.println("\n--- Lista de Pacientes ---");

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM pacientes";
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Nenhum paciente cadastrado.");
            } else {
                while (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String cpf = resultSet.getString("cpf");
                    String nascimento = resultSet.getString("nascimento");
                    String telefone = resultSet.getString("telefone");
                    String sexo = resultSet.getString("sexo");
                    String email = resultSet.getString("email");

                    System.out.println("\nNome: " + nome);
                    System.out.println("CPF: " + cpf);
                    System.out.println("Nascimento: " + nascimento);
                    System.out.println("Telefone: " + telefone);
                    System.out.println("Sexo: " + sexo);
                    System.out.println("Email: " + email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao mostrar pacientes do banco de dados: " + e.getMessage());
        }
    }
}


