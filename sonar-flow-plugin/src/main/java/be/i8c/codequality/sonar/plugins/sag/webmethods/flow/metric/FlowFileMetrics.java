package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.sonar.sslr.api.AstNode;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowMetricsVisitor;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.FlowVisitorContext;

public class FlowFileMetrics {

  private final FlowMetricsVisitor fileLinesVisitor = new FlowMetricsVisitor();
  private Integer complexity;
  private FlowGrammar[] complexityGrammar = new FlowGrammar[] { FlowGrammar.BRANCH,
      FlowGrammar.LOOP, FlowGrammar.RETRY };

  /**
   * Creates a FlowFileMetrics Object, which contains metrics that are linked with this file. It
   * contains metrics that can be evaluated by inspecting the AST tree or by scanning the file.
   * Scanning the file is used when more complex compositions are needed to calculate a metric.
   * 
   * @param context
   */
  public FlowFileMetrics(FlowVisitorContext context) {
    AstNode rootTree = context.rootTree();
    Objects.requireNonNull(rootTree, "Cannot compute metrics without a root tree");
    complexity = rootTree.getDescendants(complexityGrammar).size();
    fileLinesVisitor.scanFile(context);
  }

  public Set<Integer> linesOfCode() {
    return fileLinesVisitor.getLinesOfCode();
  }

  public Set<Integer> commentLines() {
    return fileLinesVisitor.getLinesOfComments();
  }
  
  public Map<Integer, String> dependencies() {
    return fileLinesVisitor.getDependencies();
  }

  public Integer getComplexity() {
    return complexity;
  }
  
  

}
