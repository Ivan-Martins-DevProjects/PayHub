package com.paymentJava.commands;

import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import com.paymentJava.security.EncriptKey;

interface InitActions {
  String execute(String argument);
}

public class Init implements Command {
  public void execute(ParseInput args) {
    Map<String, String> encripted = new HashMap<>();
    Map<String, String> decripted = new HashMap<>();

    try {
      Config config = ReadYaml.execute(args);

      if (config.Gateways == null || config.Gateways.isEmpty()) {
        System.out.println("Nenhum gateway encontrado");
        return;
      }

      for (Map.Entry<String, Config.Gateway> entry : config.Gateways.entrySet()) {
        String nome = entry.getKey();
        Config.Gateway gateway = entry.getValue();

        String encript = EncriptKey.encriptKey(gateway.secrets.api_key);
        encripted.put(nome, encript);

        String decript = EncriptKey.decriptKey(encript);
        decripted.put(nome, decript);
      }

      if (encripted.isEmpty()) {
        System.out.println("Nenyhuma chave foi criptografada");
      }

      System.out.println("Criptografadas: " + encripted);
      System.out.println("Descriptografadas: " + decripted);

    } catch (Exception e) {
      System.err.println("Erro: " + e.getMessage());
    }

  }
}

class ReadYaml {
  public static Config execute(ParseInput args) {
    Yaml arquivo = new Yaml();

    InputStream input = ReadYaml.class
        .getClassLoader()
        .getResourceAsStream("stripe.yml");

    if (input == null) {
      throw new RuntimeException("Arquivo YML não encontrado");
    }
    Config config = arquivo.loadAs(input, Config.class);
    return config;
  }

}

class Config {
  public Map<String, Gateway> Gateways;

  public static class Gateway {
    public Info info;
    public Secrets secrets;
    public Fallback fallback;

  }

  public static class Info {
    public String api_url;
  }

  public static class Secrets {
    public String api_key;
  }

  public static class Fallback {
    public int timeout;
    public int retries;
  }
}
