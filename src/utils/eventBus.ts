import mitt from 'mitt';

type Events = {
  'class-joined': void;
  // Add other events here if needed
};

export const eventBus = mitt<Events>();

export const EVENTS = {
  CLASS_JOINED: 'class-joined',
} as const;
