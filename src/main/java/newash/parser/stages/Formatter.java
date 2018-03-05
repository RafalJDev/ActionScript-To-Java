package newash.parser.stages;

import newash.io.code.IOEntity;

public class Formatter {

  IOEntity ioEntity = IOEntity.getInstance();

  public void replaceTwoAndMoreNewLinesWithOneLine() {
    StringBuilder sb = new StringBuilder();
    sb.append(ioEntity.getOutputCode().toString().replaceAll("\n(\\s*\\n){2}", "\n\n"));
    ioEntity.setOutputCode(sb);

  }
}
