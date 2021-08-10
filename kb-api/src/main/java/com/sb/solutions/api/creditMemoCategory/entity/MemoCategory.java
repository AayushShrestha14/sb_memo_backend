package com.sb.solutions.api.creditMemoCategory.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;
import lombok.*;

import javax.persistence.Entity;


@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoCategory extends BaseEntity<Long> {

    private String name;
    private Status status = Status.ACTIVE;

}
