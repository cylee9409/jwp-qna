package subway.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Table(name = "staion")
public class Station implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    protected Station() {

    }

    public Station(final String name) {
        this.name = name;
    }

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public Line getLine() {
        return this.line;
    }

    public void setLine(final Line line) {
        this.line = line;
        line.addStation(this);
    }
}
