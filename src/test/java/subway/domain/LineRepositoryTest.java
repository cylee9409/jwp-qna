//package subway.domain;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class LineRepositoryTest {
//
//    @Autowired
//    private LineRepository lines;
//
//    @Autowired
//    private StationRepository stations;
//
//    @Test
//    void findById() {
//        final Line line = lines.findByName("3호선");
//        assertThat(line.getStations()).isNotEmpty();
//
//    }
//
//    @Test
//    //연관 관계의 주인이 아닌 곳에서 입력된 값은 외래 키에 영향을 주지 않는다.
//    void save() {
//        final Line line = new Line("2호선");
//        final Station station = stations.save(new Station("잠실역"));
//        line.addStation(station);
//        lines.save(line);
//        lines.flush();
//    }
//
//    @Test
//    void name() {
//        final Line line = lines.save(new Line("2호선"));
//        final Station station = stations.save(new Station("잠실역"));
//        station.setLine(line);
//        lines.flush();
//        assertThat(line.getStations()).isNotEmpty();
//    }
//}
