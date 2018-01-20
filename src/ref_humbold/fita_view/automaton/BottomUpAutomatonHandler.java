package ref_humbold.fita_view.automaton;

import java.util.Objects;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class BottomUpAutomatonHandler
    extends AutomatonHandler
{
    private BottomUpDFTA automaton;
    private String label;
    private String leftValue;
    private String rightValue;
    private String nodeResult;

    @Override
    public TreeAutomaton getAutomaton()
    {
        return automaton;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        tagName = qName;
        content = new StringBuilder();

        switch(qName)
        {
            case "automaton":
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
            case "variables":
                automaton = new BottomUpDFTA(variables.values(), alphabet);
                break;

            case "accept":
                for(Integer id : variables.keySet())
                {
                    if(accept.get(variables.get(id)) == null)
                        throw new NoAcceptingForVariableException(
                            writePosition() + "Variable with ID " + id + "has no accepting value.");
                }

                automaton.addAcceptingState(accept);
                break;

            case "trans":
                automaton.addTransition(variables.get(varID), leftValue, rightValue, label,
                                        nodeResult);
                break;

            case "label":
                label = content.toString();
                content = null;

                if(!Objects.equals(label, Wildcard.EVERY_VALUE) && !alphabet.contains(label))
                    throw new IllegalAlphabetWordException(
                        writePosition() + "Given label \'" + label
                            + "\' is not a part of automaton's alphabet.");
                break;

            case "left-value":
                leftValue = content.toString();
                content = null;

                if(!Objects.equals(leftValue, Wildcard.EVERY_VALUE) && !Objects.equals(leftValue,
                                                                                       Wildcard.SAME_VALUE)
                    && !variables.get(varID).contains(leftValue))
                    throw new IllegalVariableValueException(
                        writePosition() + "Given left-value \'" + leftValue
                            + "\' is not a value of variable with ID " + varID + ".");
                break;

            case "right-value":
                rightValue = content.toString();
                content = null;

                if(!Objects.equals(rightValue, Wildcard.EVERY_VALUE) && !Objects.equals(rightValue,
                                                                                        Wildcard.SAME_VALUE)
                    && !variables.get(varID).contains(rightValue))
                    throw new IllegalVariableValueException(
                        writePosition() + "Given right-value \'" + rightValue
                            + "\' is not a value of variable with ID " + varID + ".");
                break;

            case "node-result":
                nodeResult = content.toString();
                content = null;

                if(!Objects.equals(nodeResult, Wildcard.LEFT_VALUE) && !Objects.equals(nodeResult,
                                                                                       Wildcard.RIGHT_VALUE)
                    && !variables.get(varID).contains(nodeResult))
                    throw new IllegalVariableValueException(
                        writePosition() + "Given node-result \'" + nodeResult
                            + "\' is not a value of variable with ID " + varID + ".");
                break;

            default:
                super.endElement(uri, localName, qName);
        }
    }
}
