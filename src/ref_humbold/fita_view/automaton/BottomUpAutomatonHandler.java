package ref_humbold.fita_view.automaton;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
    public void error(SAXParseException e)
        throws SAXException
    {
        throw new AutomatonParsingException(e.getMessage(), e);
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
                automaton = new BottomUpDFTA(alphabet, variables.values());
                break;

            case "accept":
                for(Integer id : variables.keySet())
                {
                    if(accept.get(variables.get(id)) == null)
                        throw new NoAcceptingForVariableException(
                            "Variable with ID " + id + "has no accepting value.");
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

                if(!label.equals(Wildcard.EVERY_VALUE) && !alphabet.contains(label))
                    throw new IllegalAlphabetWordException(
                        "Given label \'" + label + "\' is not a part of automaton's alphabet.");
                break;

            case "left-value":
                leftValue = content.toString();
                content = null;

                if(!leftValue.equals(Wildcard.EVERY_VALUE) && !leftValue.equals(Wildcard.SAME_VALUE)
                    && !variables.get(varID).contains(leftValue))
                    throw new IllegalVariableValueException(
                        "Given left-value \'" + leftValue + "\' is not a value of variable with ID "
                            + varID + ".");
                break;

            case "right-value":
                rightValue = content.toString();
                content = null;

                if(!rightValue.equals(Wildcard.EVERY_VALUE) && !rightValue.equals(
                    Wildcard.SAME_VALUE) && !variables.get(varID).contains(rightValue))
                    throw new IllegalVariableValueException("Given right-value \'" + rightValue
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                break;

            case "node-result":
                nodeResult = content.toString();
                content = null;

                if(!nodeResult.equals(Wildcard.LEFT_VALUE) && !nodeResult.equals(
                    Wildcard.RIGHT_VALUE) && !variables.get(varID).contains(nodeResult))
                    throw new IllegalVariableValueException("Given node-result \'" + nodeResult
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                break;

            default:
                super.endElement(uri, localName, qName);
        }
    }
}
