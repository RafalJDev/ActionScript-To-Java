package newash.actionscript.stage.stages;

import newash.actionscript.stage.Stage;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jaszczynski.Rafal on 01.03.2018.
 */
public class ImportStage extends Stage {
  private static ImportStage ourInstance = new ImportStage();

  public static ImportStage getInstance() {
    return ourInstance;
  }

  private ImportStage() {
    name = "ImportStage";
    code = new StringBuilder();
  }

  private Set<String> candidatesToImportSet = new HashSet<>();

  public Set<String> getCandidatesToImportSet() {
    return candidatesToImportSet;
  }

  public void setCandidatesToImportSet(Set<String> candidatesToImportSet) {
    this.candidatesToImportSet = candidatesToImportSet;
  }

  public void addCandidateToSet(String candidate) {
    candidatesToImportSet.add(candidate);
  }
}
