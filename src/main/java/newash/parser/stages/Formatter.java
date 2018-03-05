package newash.parser.stages;

import newash.io.code.IOCodeEntity;

public class Formatter {

  IOCodeEntity ioCodeEntity = IOCodeEntity.getInstance();

  public void replaceTwoAndMoreNewLinesWithOneLine() {
    StringBuilder sb = new StringBuilder();
    sb.append(ioCodeEntity.getOutputCode().toString().replaceAll("\n(\\s*\\n){2}", "\n\n"));
    ioCodeEntity.setOutputCode(sb);

  }
}
