const monthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

export const sessionDisplayDate = (date: Date): string => {
  const yyyy = date.getFullYear();
  const MMMMMMMMM = monthNames[date.getMonth()];
  const dd = date.getDate();
  return 'Session on ' + MMMMMMMMM + ' ' + dd + ', ' + yyyy;
};

export const sessionSetDate = (date: Date): string => {
  const yyyy = date.getFullYear();
  const mm =
    date.getMonth() + 1 + ''.length > 1
      ? date.getMonth() + 1
      : '0' + (date.getMonth() + 1);
  const dd =
    date.getDate() + ''.length > 1 ? date.getDate() : '0' + date.getDate();
  return yyyy + '-' + mm + '-' + dd;
};
