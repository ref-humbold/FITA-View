package fitaview.automaton;

import java.util.Objects;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class BottomUpAutomatonHandler
        extends AutomatonHandler<BottomUpAutomaton>
{
    private String label;
    private String leftValue;
    private String rightValue;
    private String nodeResult;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        tagName = qName;
        content = new StringBuilder();

        switch(qName)
        {
            case "accepting":
            case "label":
            case "left-value":
            case "right-value":
            case "node-result":
                break;

            default:
                super.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        switch(qName)
        {
            case "accepting":
                break;

            case "variables":
                result = isDeterministic
                         ? new BottomUpDfta(variables.values(), alphabet)
                         : new BottomUpNfta(variables.values(), alphabet);
                break;

            case "conditions":
                for(Integer id : variables.keySet())
                {
                    if(conditions.get(variables.get(id)) == null)
                        throw new NoAcceptanceForVariableException(
                                String.format("%s: Variable with ID %d has no acceptance condition",
                                              writePosition(), id));
                }

                result.addAcceptanceConditions(conditions);
                break;

            case "trans":
                result.addTransition(variables.get(varID), leftValue, rightValue, label,
                                     nodeResult);
                break;

            case "label":
                label = content.toString();
                content = null;

                if(!Objects.equals(label, Wildcard.EVERY_VALUE) && !alphabet.contains(label))
                    throw new IllegalAlphabetWordException(
                            String.format("%sGiven label '%s' is not a part of result's alphabet",
                                          writePosition(), label));
                break;

            case "left-value":
                leftValue = content.toString();
                content = null;

                if(!Objects.equals(leftValue, Wildcard.EVERY_VALUE) && !Objects.equals(leftValue,
                                                                                       Wildcard.SAME_VALUE)
                           && !variables.get(varID).contains(leftValue))
                    throw new IllegalVariableValueException(String.format(
                            "%s Given left-value '%s' is not a value of variable with ID %d",
                            writePosition(), leftValue, varID));
                break;

            case "right-value":
                rightValue = content.toString();
                content = null;

                if(!Objects.equals(rightValue, Wildcard.EVERY_VALUE) && !Objects.equals(rightValue,
                                                                                        Wildcard.SAME_VALUE)
                           && !variables.get(varID).contains(rightValue))
                    throw new IllegalVariableValueException(String.format(
                            "%s Given right-value '%s' is not a value of variable with ID %d",
                            writePosition(), rightValue, varID));
                break;

            case "node-result":
                nodeResult = content.toString();
                content = null;

                if(!Objects.equals(nodeResult, Wildcard.LEFT_VALUE) && !Objects.equals(nodeResult,
                                                                                       Wildcard.RIGHT_VALUE)
                           && !variables.get(varID).contains(nodeResult))
                    throw new IllegalVariableValueException(String.format(
                            "%s Given node-result '%s' is not a value of variable with ID %d",
                            writePosition(), nodeResult, varID));
                break;

            default:
                super.endElement(uri, localName, qName);
        }
    }
}
