package de.uni_passau.sds.patterns;

import de.uni_passau.sds.patterns.converter.*;
import org.apache.commons.text.StringEscapeUtils;

public class PatternConverter {

    public static String rewrite(String ecmaScriptPattern) throws REException {
        ExpressionTree ecmaScriptTree;
        try {
            ecmaScriptTree = Builder.buildExpressionTree(StringEscapeUtils.unescapeJson(ecmaScriptPattern));
        } catch (ECMAScriptNotSupportedException e) {
            throw new REException(e.getMessage(), e.getType() == 0 ? REException.REG_ELOOK : REException.REG_NSUPP, -1);
        } catch (ECMAScriptRangeException e) {
            throw new REException(e.getMessage(), e.getType() == 0 ? REException.REG_ERANGE : REException.REG_EBRACK, -1);
        } catch (ECMAScriptSyntaxException e) {
            throw new REException(e.getMessage(), e.getType() == 0 ? REException.REG_EEND : -1, -1);
        }
        ExpressionTree bricsTree = EcmaToBricsTreeConverter.convert(ecmaScriptTree);
        return bricsTree.convertToString();
    }

}
