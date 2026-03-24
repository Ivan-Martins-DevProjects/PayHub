import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jline.reader.*;
import org.jline.terminal.*;

import com.paymentJava.commands.Command;
import com.paymentJava.commands.Init;
import com.paymentJava.commands.ParseInput;
import com.paymentJava.commands.SetText;

public class Main {
  public static void main(String[] args) {
    Map<String, Command> commands = new HashMap<>();

    commands.put("set", new SetText());
    commands.put("init", new Init());

    try {
      Terminal terminal = TerminalBuilder.builder().system(true).build();
      LineReader reader = LineReaderBuilder.builder()
          .terminal(terminal)
          .build();

      String input;

      while ((input = reader.readLine("prompt> ")) != null) {
        String[] partes = input.split(" ");
        Command cmd = commands.get(partes[0]);
        ParseInput newInput = new ParseInput(partes);

        if (cmd != null) {
          cmd.execute(newInput);
        } else if (partes[0].equalsIgnoreCase("exit")) {
          terminal.writer().println("Encerrando...");
          break;
        }

      }

      terminal.close();
    } catch (IOException e) {
      System.err.println("Error ao criar terminal: " + e.getMessage());
    }
  }
}
