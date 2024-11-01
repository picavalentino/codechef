$(document).ready(function() {
    let currentDate = new Date(); // 현재 날짜
    let currentMonth = currentDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더합니다.
    let currentYear = currentDate.getFullYear();
    let endDate = null;

    // 초기 캘린더 로드
    loadCalendar(currentMonth, currentYear);

    function loadCalendar(month, year) {
        $.ajax({
            url: '/reservation/ajax',
            method: 'GET',
            data: { month: month, year: year },
            success: function(data) {
                $('.date_info').text(data.month + ' ' + data.year);
                $('.calendar .day').not('.weekday').remove();

                let daysInMonth = data.days;
                let startDate = new Date(currentYear, currentMonth - 1, currentDate.getDate()); // 현재 날짜

                if(endDate == null){
                    endDate = new Date(currentYear, currentMonth, currentDate.getDate()); // 다음 달 같은 날

                    // endDate를 다음 달의 마지막 날로 설정
//                    endDate.setDate(endDate.getDate() + 1);
                    endDate.setDate(endDate.getDate());
                }

                let previousDate = new Date(currentDate); // 원본 날짜를 복사
                previousDate.setDate(previousDate.getDate() - 1); // 하루 빼기

                let emptyDay = getEmptyDay(data.week);
                $('.calendar').append(emptyDay);
                daysInMonth.forEach(function(day) {
                    let dayDate = new Date(year, month - 1, day); // month - 1 (0-indexed)
                    let dayElement = $('<div class="day" onclick="toggleDate(this)" name="' + day + '"><span>' + day + '</span></div>');

                    // 현재 날짜의 이전 날짜 및 활성화 범위를 초과하는 날짜를 비활성화
                    if (dayDate < previousDate || dayDate >= endDate) {
                        dayElement.addClass('disabled'); // 비활성화된 스타일 추가
                    }

                    $('.calendar').append(dayElement);
                });
            },
            error: function(err) {
                console.error("Error loading calendar", err);
            }
        });
    }

    function getEmptyDay(week) {
        let emptyDays = '';
        let emptyDay = '<div class="day" style="cursor: auto;"><span></span></div>';

        switch(week){
            case "SATURDAY":
                emptyDays += emptyDay;
            case "FRIDAY":
                emptyDays += emptyDay;
            case "THURSDAY":
                emptyDays += emptyDay;
            case "WEDNESDAY":
                emptyDays += emptyDay;
            case "TUESDAY":
                emptyDays += emptyDay;
            case "MONDAY":
                emptyDays += emptyDay;
        }

        return emptyDays;
    }

    // 이전 및 다음 달 버튼 클릭 이벤트
    $('#prevMonth').click(function() {
        if (currentMonth === 1) {
            currentMonth = 12;
            currentYear--;
        } else {
            currentMonth--;
        }
        loadCalendar(currentMonth, currentYear);
    });

    $('#nextMonth').click(function() {
        if (currentMonth === 12) {
            currentMonth = 1;
            currentYear++;
        } else {
            currentMonth++;
        }
        loadCalendar(currentMonth, currentYear);
    });

    $('.dropdown-item').click(function(event) {
        event.preventDefault(); // 링크 기본 동작 방지
        const numPeople = $(this).data('value'); // data-value 속성에서 인원 수 가져오기

        const element = $("body > div:nth-of-type(2) > div:nth-of-type(1) > div > form > div > div:nth-of-type(2) > div:nth-of-type(2) > label > b");
        if(numPeople !== "인원 수"){
            element.text("인원 수");
        } else {
            element.text("");
        }

        $('#numPeople').val(numPeople); // 숨겨진 input에 저장
        $('.btn-group .btn:first-child').html(`<b><small>${numPeople}</small></b>`); // 버튼 텍스트 업데이트
    });

    $('#reservation_btn').click(function() {
        let selectedDay = null;
        let reservation_num = $('#numPeople').val();
        let reservation_time = $('.time_btn.selected').text();

        const days = document.querySelectorAll('.day');

        // 각 요소를 순회하면서 lightgray 색상이 적용된 요소의 name 속성을 가져오기
        days.forEach(day => {
            if (getComputedStyle(day).color === 'rgb(211, 211, 211)') { // lightgray의 RGB 값
                selectedDay = day.getAttribute('name');
            }
        });

        if(selectedDay == null){
            alert("날짜를 선택해 주세요");
            return;
        } else if(reservation_time === '') {
            alert("시간을 선택해 주세요");
            return;
        }
        else if(reservation_num == "" || reservation_num == "인원 수"){
            alert("인원을 선택해 주세요");
            return;
        }

        let date = new Date(currentYear, currentMonth - 1, selectedDay);

        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const weekdays = ['일', '월', '화', '수', '목', '금', '토'];
        const weekday = weekdays[date.getDay()];

        const formattedDate = `${month}.${day}(${weekday})`;

        const [hours, minutes] = reservation_time.split(":").map(Number);

        // 주어진 날짜에 시간과 분을 설정합니다.
        date.setHours(hours);
        date.setMinutes(minutes);

        const localDateTime = new Date(date.getTime() - (date.getTimezoneOffset() * 60000)); // UTC로 변환
        $('#selectedDay').val(localDateTime.toISOString().slice(0, 19)); // "2024-10-30T19:00:00" 형식으로 설정

        $('#select_time').val(reservation_time);

        $('#reservation_date').html(formattedDate);
        $('#reservation_time').html(reservation_time);
        $('#reservation_num').html(reservation_num + "명");

        $('#confirmModal').modal('show');
    });

    $('#confirmButton').click(function() {
        $('#confirmModal').modal('hide'); // 모달 닫기

        $('#reservationForm').submit();
    });

     $('#cancelButton').on('click', function() {
        $('#confirmModal').modal('hide');
    });
});

function toggleDate(element) {
    if ($(element).hasClass('disabled')) {
        return; // 비활성화된 날짜는 클릭 무시
    }

    const selectedDay = document.querySelector('.day.selected');

    if (selectedDay) {
        if (selectedDay === element) {
            // 이미 선택된 날짜를 클릭하면 해제
            selectedDay.classList.remove('selected');
        } else {
            // 다른 날짜를 클릭하면 이전 선택 해제
            selectedDay.classList.remove('selected');
            element.classList.add('selected');
            select_day(element);
        }
    } else {
        // 아무 것도 선택되지 않은 상태에서 클릭
        element.classList.add('selected');
        select_day(element);
    }
}

function toggleTime(element) {
    const selectedTime = document.querySelector('.time_btn.selected');

    if (selectedTime) {
        if (selectedTime === element) {
            // 이미 선택된 시간을 클릭하면 해제
            selectedTime.classList.remove('selected');
        } else {
            // 다른 시간을 클릭하면 이전 선택 해제
            selectedTime.classList.remove('selected');
            element.classList.add('selected');
        }
    } else {
        element.classList.add('selected');
    }
}

function getMonthWeekNumber(date) {
    const currentDate = date.getDate();
    const firstDay = new Date(date.setDate(1)).getDay();

    return Math.ceil((currentDate + firstDay) / 7);
}

function select_day(element) {
    const dateInfo = $('.date_info').text() + " " + element.textContent;

    const week_find_date = new Date(dateInfo);
    const weekNumber = getMonthWeekNumber(week_find_date);

    const koreanDayOfWeek = weekNumber + "주" + getKoreanDayOfWeek(dateInfo);
    const chefNo = $('#chefNo').val();

    console.log(koreanDayOfWeek);

////////// 현재 날짜를 선택했을 경우 예약시간이 현재 시간보다 이전인지 확인하는 부분
   // dateInfo를 Date 객체로 변환
   const [monthStr, yearStr, dayStr] = dateInfo.split(" ");
   const month = new Date(`${monthStr} 1, ${yearStr}`).getMonth(); // 월을 숫자로 변환
   const day = parseInt(dayStr, 10);
   const year = parseInt(yearStr, 10);

   const dateFromInfo = new Date(year, month, day);

   // 현재 날짜 가져오기
   const currentDate = new Date();
   const currentHour = currentDate.getHours();
   const currentMinute = currentDate.getMinutes();

   // 현재 시간에서 1시간 더하기
   const oneHourAfter = new Date(currentDate);
   oneHourAfter.setHours(currentHour + 1);

   // 한 시간 이후의 시간 객체로 만들기
   const oneHourAfterTime = new Date(oneHourAfter.getFullYear(), oneHourAfter.getMonth(), oneHourAfter.getDate(), oneHourAfter.getHours(), oneHourAfter.getMinutes());

   // 현재 날짜와 비교
   const isCurrentDate =
       dateFromInfo.getFullYear() === currentDate.getFullYear() &&
       dateFromInfo.getMonth() === currentDate.getMonth() &&
       dateFromInfo.getDate() === currentDate.getDate();
//////////

    const queryParams = {
        chef_no: chefNo,
        koreanDayOfWeek: koreanDayOfWeek
    };

    $.getJSON('/reservation/timeSlot', queryParams, function(data){
        const str = data.timeSlotDTOS;
        const timeSlots = parseTimeSlotDTO(str);

        $('.time_select').html('');
        for(let i = 0; i < timeSlots.length; i++){
            if(timeSlots[i].available === "false" && timeSlots[i].time !== "휴무"){
                if(isCurrentDate){
                    const timeSlotTime = timeSlots[i].time;

                    const [hour, minute] = timeSlotTime.split(":");
                    const timeSlotDate = new Date(oneHourAfterTime.getFullYear(), oneHourAfterTime.getMonth(), oneHourAfterTime.getDate(), hour, minute);

                    // 예약가능 시간이 현재 시간보다 이전이면 넘어가기
                    if(timeSlotDate < oneHourAfterTime){
                        continue;
                    }
                }
                $('.time_select').append('<button type="button" class="btn time_btn" onclick="toggleTime(this)">'+timeSlots[i].time+'</button>');
            } else if (timeSlots[i].time === "휴무"){
                $('.time_select').append('<button type="button" class="btn time_btn" disabled>'+timeSlots[i].time+'</button>');
            } else {
                $('.time_select').append('<button type="button" class="btn time_btn" disabled>'+timeSlots[i].time+'예약</button>');
            }
        }
    });
}

function parseTimeSlotDTO(str) {
    return str
        .slice(1, -1) // 대괄호 제거
        .split('), ') // 객체 분리
        .map(slot => {
            // 각 슬롯을 적절한 객체 형태로 변환
            const properties = slot.replace('TimeSlotDTO(', '').split(', '); // 속성 분리
            const obj = {};
            properties.forEach(prop => {
                const [key, value] = prop.split('='); // 키와 값 분리
                obj[key.trim()] = value.trim().replace(/^(false|true)$/, (match) => match === 'true'); // boolean 변환

                // 숫자 변환 및 ')' 제거
                if (key.trim() === 'chefNo') {
                    obj[key.trim()] = parseInt(value.trim().replace(/\D/g, '')); // 숫자로 변환하고 non-digit 제거
                } else if (!isNaN(value)) {
                    obj[key.trim()] = parseInt(value); // 숫자 변환
                }
            });
            return obj;
        });
}

function getKoreanDayOfWeek(dateString) {
    const date = new Date(dateString);

    // 요일 배열 (한국어)
    const daysInKorean = ['일', '월', '화', '수', '목', '금', '토'];

    // 요일 인덱스 가져오기
    const dayOfWeekIndex = date.getDay();

    // 한국어 요일 반환
    return daysInKorean[dayOfWeekIndex];
}

