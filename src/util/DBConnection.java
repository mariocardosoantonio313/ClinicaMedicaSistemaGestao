package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe utilitária para gerenciar a conexão JDBC com o banco de dados MySQL.
 * Suporta configuração via arquivo properties e modo offline/demo.
 */
public class DBConnection {
    
    private static String URL = "jdbc:mysql://localhost:3306/clinica_medica?useSSL=false&serverTimezone=UTC";
    private static String USER = "root";
    private static String PASSWORD = "";
    
    private static boolean isOnlineMode = false;
    private static boolean debugEnabled = true;
    private static boolean driverAvailable = false;
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        carregarConfiguracao();
        carregarDriver();
    }

    /**
     * Carrega configurações do arquivo database.properties
     */
    private static void carregarConfiguracao() {
        try {
            InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("database.properties");
            
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                
                String host = props.getProperty("db.host", "localhost");
                String port = props.getProperty("db.port", "3306");
                String dbName = props.getProperty("db.name", "clinica_medica");
                String ssl = props.getProperty("db.ssl", "false");
                String timezone = props.getProperty("db.timezone", "UTC");
                
                USER = props.getProperty("db.user", "root");
                PASSWORD = props.getProperty("db.password", "");
                debugEnabled = Boolean.parseBoolean(props.getProperty("debug.enabled", "true"));
                
                URL = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=%s",
                    host, port, dbName, ssl, timezone);
                
                if (debugEnabled) {
                    System.out.println("✓ Arquivo database.properties carregado com sucesso");
                    System.out.println("  Host: " + host);
                    System.out.println("  Porta: " + port);
                    System.out.println("  Banco: " + dbName);
                }
                input.close();
            } else {
                if (debugEnabled) {
                    System.out.println("⚠ Arquivo database.properties não encontrado. Usando configuração padrão.");
                }
            }
        } catch (IOException ex) {
            System.err.println("⚠ Erro ao carregar database.properties: " + ex.getMessage());
            System.out.println("  Usando configuração padrão.");
        }
    }

    /**
     * Carrega o driver JDBC do MySQL
     */
    private static void carregarDriver() {
        try {
            Class.forName(DRIVER_CLASS);
            driverAvailable = true;
            if (debugEnabled) {
                System.out.println("Driver JDBC MySQL encontrado: " + DRIVER_CLASS + ".");
            }
        } catch (ClassNotFoundException ex) {
            driverAvailable = false;
            if (debugEnabled) {
                System.err.println("Driver JDBC MySQL não encontrado. O sistema seguirá em modo de demonstração.");
                System.err.println("Coloque 'mysql-connector-java.jar' no classpath se desejar usar o banco de dados MySQL.");
            }
        }
    }

    private DBConnection() {
        // Construtor privado para evitar instância.
    }

    /**
     * Inicializa a conexão com o banco de dados
     */
    public static void initialize() {
        System.out.println("\n=== Inicializando Conexão com Banco de Dados ===");
        System.out.println("URL: " + URL);
        System.out.println("Usuário: " + USER);
        if (!driverAvailable) {
            isOnlineMode = false;
            if (debugEnabled) {
                System.err.println("Driver JDBC ausente. Entrando em modo demonstração (dados em memória).\n");
            }
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            isOnlineMode = true;
            System.out.println("✓ SUCESSO: Conectado ao banco de dados MySQL");

            try {
                String version = conn.getMetaData().getDatabaseProductVersion();
                System.out.println("  Versão MySQL: " + version);
            } catch (SQLException ex) {
                System.out.println("  ⚠ Não foi possível obter versão do MySQL");
            }
        } catch (SQLException ex) {
            isOnlineMode = false;
            System.err.println("Falha ao conectar ao banco de dados MySQL.");
            System.err.println("Detalhes: " + ex.getMessage());

            if (ex.getMessage().contains("Unknown host")) {
                System.err.println("   → Verifique se o MySQL está rodando em localhost");
            } else if (ex.getMessage().contains("Access denied")) {
                System.err.println("   → Verifique usuário e senha no arquivo database.properties");
            } else if (ex.getMessage().contains("database") && ex.getMessage().contains("doesn't exist")) {
                System.err.println("   → Banco de dados 'clinica_medica' não existe");
                System.err.println("   → Execute o script SQL de criação do banco");
            }

            System.err.println("Usando modo demonstração (dados em memória) para que a aplicação continue funcionando.\n");
        }
    }

    /**
     * Obtém uma conexão com o banco de dados
     * @return 
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (!isOnlineMode) {
            throw new SQLException("Banco de dados offline. Sistema operando em modo demo.");
        }
        
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (debugEnabled) {
                System.out.println("✓ Nova conexão obtida");
            }
            return conn;
        } catch (SQLException ex) {
            isOnlineMode = false;
            throw new SQLException("Não foi possível conectar ao banco de dados", ex);
        }
    }
    
    /**
     * Verifica se está em modo online (conectado ao MySQL)
     * @return 
     */
    public static boolean isOnline() {
        return isOnlineMode;
    }
    
    /**
     * Retorna informações de configuração
     * @return 
     */
    public static String getConfigInfo() {
        String mode = isOnlineMode ? "Conectado" : "Modo demonstração";
        String driverStatus = driverAvailable ? "driver disponível" : "driver ausente";
        String host = URL.split("://")[1].split("/")[0];
        String db = URL.split("/")[3].split("\\?")[0];
        return String.format("%s — %s | Host: %s | BD: %s | Usuário: %s",
            mode,
            driverStatus,
            host,
            db,
            USER);
    }

    public static boolean isDriverAvailable() {
        return driverAvailable;
    }

    /**
     * Testa a conexão com MySQL sem quebrar em caso de falha
     * @return true se conectado com sucesso, false caso contrário
     */
    public static boolean testConnection() {
        if (!driverAvailable || !isOnlineMode) {
            return false;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return conn.isValid(2);
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Verifica se as tabelas principais existem no banco de dados
     * @return true se tabelas encontradas, false caso contrário
     */
    public static boolean verifyTables() {
        if (!isOnlineMode) {
            return false;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String[] expectedTables = {"usuario", "paciente", "medico", "consulta", "prontuario"};
            for (String table : expectedTables) {
                String checkSql = "SELECT 1 FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
                try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                    ps.setString(1, table);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Retorna o estado de saúde do sistema (diagnosticar problemas)
     * @return 
     */
    public static String getHealthStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== Diagnóstico do Sistema ===\n");
        status.append("Driver JDBC: ").append(driverAvailable ? "Disponível" : "Ausente").append("\n");
        status.append("Modo: ").append(isOnlineMode ? "Conectado" : "Demonstração").append("\n");

        if (isOnlineMode) {
            status.append("Conexão: OK\n");
            status.append("Tabelas: ").append(verifyTables() ? "Válidas" : "Inválidas ou ausentes").append("\n");
        } else {
            status.append("Conexão: Indisponível\n");
            status.append("Dados: Em memória (demo)\n");
        }
        return status.toString();
    }
}
