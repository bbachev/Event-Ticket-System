import com.search.document.EventDocument;
import com.search.messaging.CdcEventConsumer;
import com.search.repository.EventSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CdcEventConsumerTest {

    @InjectMocks
    private CdcEventConsumer cdcEventConsumer;

    @Mock
    private EventSearchRepository eventSearchRepository;

    @Test
    public void test_shouldReturnWhenEnvelopeIsNull(){
        this.cdcEventConsumer.consume(null);
        Mockito.verifyNoInteractions(eventSearchRepository);
    }

    @Test
    public void test_shouldReturnWhenPayloadIsNull(){
        HashMap mockMap = Mockito.mock(HashMap.class);

        this.cdcEventConsumer.consume(mockMap);
        Mockito.verifyNoInteractions(eventSearchRepository);
    }

    @Test
    public void test_shouldSkipWhenDeleteEventWhenBeforeIsNull(){
        HashMap mockMap = Mockito.mock(HashMap.class);
        Map<String, Object> payload = new HashMap<>();
        payload.put("op", "d");
        Mockito.when(mockMap.get("payload")).thenReturn(payload);
        this.cdcEventConsumer.consume(mockMap);
        Mockito.verifyNoInteractions(eventSearchRepository);
    }

    @Test
    public void test_shouldDeleteWhenIdIsPresent(){
        HashMap<String, Object> mockMap = new HashMap();
        Map<String, Object> payload = new HashMap<>();
        payload.put("op", "d");

        Map<String, Object> before = new HashMap<>();
        before.put("id", "id");
        payload.put("before", before);
        mockMap.put("payload", payload);

        Optional<EventDocument> eventDocumentOptional = Optional.of(new EventDocument());
        eventDocumentOptional.get().setId("id");

        Mockito.when(eventSearchRepository.findById(any(String.class))).thenReturn(eventDocumentOptional);
        this.cdcEventConsumer.consume(mockMap);
        Mockito.verify(eventSearchRepository).findById("id");
        Mockito.verify(eventSearchRepository).delete(eventDocumentOptional.get());
    }

    @Test
    public void test_shouldReturnWhenAfterIsNull(){
        HashMap<String, Object> mockMap = new HashMap();
        Map<String, Object> payload = new HashMap<>();


        mockMap.put("payload", payload);
        this.cdcEventConsumer.consume(mockMap);
        Mockito.verifyNoInteractions(eventSearchRepository);
    }

    @Test
    public void test_shouldSaveDocument(){
        HashMap<String, Object> mockMap = new HashMap();
        Map<String, Object> payload = new HashMap<>();

        Map<String, Object> after = new HashMap<>();
        after.put("id", "id");
        mockMap.put("payload", payload);
        payload.put("after", after);

        EventDocument eventDocumentMock = new EventDocument();
        eventDocumentMock.setId("id");
        this.cdcEventConsumer.consume(mockMap);

        Mockito.verify(eventSearchRepository).save(eventDocumentMock);
    }
}
