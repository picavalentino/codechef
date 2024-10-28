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
                    endDate.setDate(endDate.getDate() + 1);
                    // endDate.setHours(0, 0, 0, 0); // 시간 초기화
                }

                let previousDate = new Date(currentDate); // 원본 날짜를 복사
                previousDate.setDate(previousDate.getDate() - 1); // 하루 빼기

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
        $('#numPeople').val(numPeople); // 숨겨진 input에 저장
        $('.btn-group .btn:first-child').html(`<b><small>${numPeople}</small></b>`); // 버튼 텍스트 업데이트
    });

    $('#reservation_btn').click(function() {
        let selectedDay = '';

        // 모든 .day 요소를 가져오기
        const days = document.querySelectorAll('.day');

        // 각 요소를 순회하면서 lightgray 색상이 적용된 요소의 name 속성을 가져오기
        days.forEach(day => {
            if (getComputedStyle(day).color === 'rgb(211, 211, 211)') { // lightgray의 RGB 값
                selectedDay = day.getAttribute('name');
            }
        });

        let date = new Date(currentYear, currentMonth - 1, selectedDay);

        $('#selectedDay').val(date);
        $('#reservationForm').submit();
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
        }
    } else {
        // 아무 것도 선택되지 않은 상태에서 클릭
        element.classList.add('selected');
    }
}