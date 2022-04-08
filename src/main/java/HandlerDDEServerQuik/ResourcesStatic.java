package HandlerDDEServerQuik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcesStatic {
    //=========���� �����, ��� ��������� �������� ������ �� ��� �����
    protected static final String pathFileBoot = "F://�������� �����/A-Trade/BootFromJavaDeals.txt";

    //=========���� �����, ������ �� QUIK DDEServer
    protected static final String pathFileDDEServer = "F://�������� �����/A-Trade/pathFileDDEServer.txt";

    //����� �������� ��� ������ �������� ������, ��� ������� ���������. ��������� �� pathFileBoot
    protected static List<Contract> bootContractList = new ArrayList<>();

    //����� �������� ���������� ������ ������, ��� ������� ���������.
    protected static Map<String, Contract> tempContractMap = new HashMap<>();

    //����� �������� �����. �� ����� ���� ��������
    protected static Map<String,Float> rateInstrument = new HashMap<>();

    //���� � ����� � ���. ��������� ������ � ������� ������� ������, ���. ��������� � ������� �������� ������
    protected static final String pathFileReportBroker = "F://�������� �����//A-Trade//" +
            "��� ������ �� ������ ������� �� 28������� 2021����.txt";

    //���� � ����� � ���. ��������� ������ � ������� ������� ������
    protected static final String pathFileIdealDeal = "F://�������� �����//A-Trade//3.txt";

    //������� ���������� ������ ������
    protected static int strokesNumber = 0;
}
