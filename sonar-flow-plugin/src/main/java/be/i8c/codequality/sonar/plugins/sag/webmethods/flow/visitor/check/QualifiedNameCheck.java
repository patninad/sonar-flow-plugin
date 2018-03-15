/*
 * i8c
 * Copyright (C) 2016 i8c NV
 * mailto:contact AT i8c DOT be
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check;

import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.sslr.FlowGrammar;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils.FlowUtils;
import be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type.FlowCheck;

import com.sonar.sslr.api.AstNode;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

/**
 * Checks Qualified name of the service for naming convention.
 * @author DEWANST
 *
 */
@Rule(key = "S00005",
    name = "Flow assets should follow the predefined naming convention",
    priority = Priority.MAJOR,
    tags = {Tags.BAD_PRACTICE })
@SqaleConstantRemediation("1min")
public class QualifiedNameCheck extends FlowCheck {

  static final Logger logger = LoggerFactory.getLogger(QualifiedNameCheck.class);

  private static final String DEFAULT_NC 
      = "[A-Z][a-z0-9]*[A-Z0-9][a-z0-9]+[A-Z.a-z0-9]*:[a-z]+[A-Z0-9][a-z0-9]+[A-Za-z0-9]*";

  @RuleProperty(key = "Naming convention",
      description = "Regular expression defining the naming convention of flow assets",
      defaultValue = "" + DEFAULT_NC)
  private String namingConvention = DEFAULT_NC;
  private Pattern pattern;

  @Override
  public void init() {
    logger.debug("++ Initializing {} ++", this.getClass().getName());
    pattern = Pattern.compile(namingConvention);
    subscribeTo(FlowGrammar.FLOW);
  }

  @Override
  public void visitNode(AstNode astNode) {
    String service = FlowUtils.getQualifiedName(this.getContext().getFile());
    logger.debug("Service found: " + service);
    if (!pattern.matcher(service).matches()) {
      getContext().createLineViolation(this,
          "Flow name " + service + " does not conform to the naming convention", astNode);
    }
  }

  @Override
  public boolean isFlowCheck() {
    return true;
  }

  @Override
  public boolean isNodeCheck() {
    return false;
  }

  @Override
  public boolean isTopLevelCheck() {
    return false;
  }
}