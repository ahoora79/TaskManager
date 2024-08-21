package taskmanager.taskmanager.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String localDate;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_username")
    private User user;

    public Task(String title , String description , String localDate , String status,User user){
        this.title = title;
        this.description = description;
        this.localDate = localDate;
        this.status = status;
        this.user = user;
    }
}
