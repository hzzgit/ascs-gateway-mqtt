package net.fxft.ascsgatewaymqckbserver.common;

import lombok.*;

import java.io.Serializable;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/


@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
public class TranDataProto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int flag;
	private Integer sequence;
	private Integer code;
    private String content;
    
    public TranDataProto() {	
    } 
        
}