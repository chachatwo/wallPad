package com.wallpad.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.wallpad.project.dto.NotificationDTO;

@Mapper
public interface NotificationMapper {

    void insertNotification(NotificationDTO dto);

    List<NotificationDTO> selectUnreadNotifications(String apartmentNumber);

    int countUnreadNotifications(String apartmentNumber);

    void updateAllReadStatus(String apartmentNumber);
    
    List<NotificationDTO> selectLatestNotifications(String apartmentNumber);


}
