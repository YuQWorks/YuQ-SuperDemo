package wiki.IceCream.yuq.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@Table(name = "qqGroup")
@Entity
public class QqGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Long groupId;
    @Column(nullable = false)
    private Boolean recall;
    @Column(nullable = false)
    private Boolean ad;
    @Column(nullable = false)
    private ArrayList<String> adKeyList;
    @Column(nullable = false)
    private Boolean autoAccept;


}
