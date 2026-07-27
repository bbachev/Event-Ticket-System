import com.search.document.EventDocument;
import com.search.dto.request.EventSearchRequest;
import com.search.service.impl.EventSearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class EventSearchServiceImplTest {

    @InjectMocks
    private EventSearchServiceImpl eventSearchService;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    private static final EventSearchRequest REQUEST = new EventSearchRequest(
            null, "MUSIC", "Sofia", null, null, null, null,
            null, null, null, null, null
    );

    @Test
    public void searchDocuments_returnsEmptyList_whenNoHits() {
        SearchHits<EventDocument> mockHits = Mockito.mock(SearchHits.class);
        Mockito.when(mockHits.getSearchHits()).thenReturn(List.of());
        Mockito.when(elasticsearchOperations.search(any(CriteriaQuery.class), eq(EventDocument.class)))
                .thenReturn(mockHits);

        List<EventDocument> result = eventSearchService.searchDocuments(REQUEST);

        assertTrue(result.isEmpty());
    }

    @Test
    public void searchDocuments_returnsMappedDocuments_whenHitsPresent() {
        EventDocument eventDocument = new EventDocument();
        eventDocument.setId("id");
        eventDocument.setCategory("MUSIC");

        SearchHit<EventDocument> mockHit = Mockito.mock(SearchHit.class);
        Mockito.when(mockHit.getContent()).thenReturn(eventDocument);

        SearchHits<EventDocument> mockHits = Mockito.mock(SearchHits.class);
        Mockito.when(mockHits.getSearchHits()).thenReturn(List.of(mockHit));
        Mockito.when(elasticsearchOperations.search(any(CriteriaQuery.class), eq(EventDocument.class)))
                .thenReturn(mockHits);

        List<EventDocument> result = eventSearchService.searchDocuments(REQUEST);

        assertEquals(1, result.size());
        assertEquals("id", result.get(0).getId());
    }
}
